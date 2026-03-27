package com.cisco.webexcc.dbconnector.service;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.SqlStatement;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class SqlExecutionService {

    private static final Pattern NOTES_EQUALS_PATTERN = Pattern.compile("(?i)\\bNOTES\\s*=\\s*(\\?|:[A-Za-z_][A-Za-z0-9_]*|'(?:''|[^'])*')");
    private static final Pattern SQL_LEADING_COMMENTS_PATTERN = Pattern.compile("(?is)^\\s*(?:--.*?(?:\\r?\\n|$)|/\\*.*?\\*/\\s*)+");
        private static final Pattern ORACLE_TEXT_LIKE_BIND_PATTERN = Pattern.compile("(?i)\\b(?:telephone|phone|mobile|msisdn|username|email|notes|metadata_json)\\b\\s*=\\s*\\?");
    private static final Pattern ORACLE_TEXT_LIKE_NUMERIC_LITERAL_PATTERN = Pattern.compile("(?i)\\b(telephone|phone|mobile|msisdn|username|email|notes|metadata_json)\\b\\s*=\\s*(-?\\d+(?:\\.\\d+)?)\\b");
        private static final Set<String> ORACLE_STRING_PARAM_HINTS = Set.of(
            "telephone", "phone", "mobile", "msisdn", "username", "email", "notes", "metadata", "json", "name"
        );

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

    @Value("${can_update:false}")
    private boolean canUpdate;

    @Value("${can_delete:false}")
    private boolean canDelete;

    @Value("${can_insert:false}")
    private boolean canInsert;

    public List<Map<String, Object>> executeSql(String name, String env, Map<String, Object> params) {
        SqlStatement statement = sqlStatementRepository.findByNameIgnoreCaseAndEnvironment(name, env)
                .orElseThrow(() -> new RuntimeException("SQL Statement not found: " + name + " in " + env));

        DbConnection conn = statement.getDbConnection();
        
        // Handle positional parameters mapping if defined AND SQL contains '?'
        if (statement.getParamNames() != null && !statement.getParamNames().isEmpty() 
                && statement.getSqlContent() != null && statement.getSqlContent().contains("?")) {
            String[] paramNames = statement.getParamNames().split(",");
            List<Object> positionalParams = new java.util.ArrayList<>();
            for (String paramName : paramNames) {
                paramName = paramName.trim();
                if (params.containsKey(paramName)) {
                    positionalParams.add(params.get(paramName));
                } else {
                    throw new IllegalArgumentException("Missing required parameter: " + paramName);
                }
            }
            return executeRawSql(conn, statement.getSqlContent(), params, positionalParams);
        }
        
        return executeRawSql(conn, statement.getSqlContent(), params, Collections.emptyList());
    }

    // Overload for backward compatibility if needed, or just default to empty map
    public List<Map<String, Object>> executeSql(String name, String env) {
        return executeSql(name, env, Collections.emptyMap());
    }

    public List<Map<String, Object>> executeRawSql(DbConnection conn, String sql, Map<String, Object> params) {
        return executeRawSql(conn, sql, params, Collections.emptyList());
    }

    public List<Map<String, Object>> executeRawSql(DbConnection conn, String sql, Map<String, Object> params, List<Object> positionalParams) {
        String effectiveSql = normalizeSqlForJdbc(sql);
        boolean isOracle = conn.getType() == DbConnection.DbType.ORACLE;
        if (isOracle) {
            effectiveSql = normalizeOracleTextComparisons(effectiveSql);
        }
        DataSource dataSource = createDataSource(conn);
        List<Object> oraclePositionalParams = isOracle
            ? coerceOraclePositionalStringParams(effectiveSql, positionalParams)
            : positionalParams;
        Map<String, Object> oracleNamedParams = isOracle
            ? coerceOracleNamedStringParams(params)
            : params;
        List<Object> effectivePositionalParams = isOracle
            ? normalizeOraclePositionalParams(oraclePositionalParams)
            : oraclePositionalParams;
        Map<String, Object> effectiveNamedParams = isOracle
            ? normalizeOracleNamedParams(oracleNamedParams)
            : oracleNamedParams;
        try {
            return runQuery(dataSource, effectiveSql, effectiveNamedParams, effectivePositionalParams);
        } catch (DataAccessException ex) {
            String message = ex.getMessage();
            if (isOracle && message != null && message.contains("ORA-22848")) {
                String rewrittenSql = rewriteOracleClobEquals(effectiveSql);
                if (!rewrittenSql.equals(effectiveSql)) {
                    return runQuery(dataSource, rewrittenSql, effectiveNamedParams, effectivePositionalParams);
                }
                throw new IllegalArgumentException(
                        "Oracle CLOB columns cannot be compared with '='. Use DBMS_LOB.COMPARE(clob_col, TO_CLOB(?)) = 0 " +
                        "for exact match, or DBMS_LOB.INSTR(clob_col, ?) > 0 for contains.",
                        ex
                );
            }
            if (isOracle && message != null && message.contains("ORA-01722")) {
                throw new IllegalArgumentException(buildOracleInvalidNumberMessage(effectiveSql, effectiveNamedParams, effectivePositionalParams), ex);
            }
            throw ex;
        }
    }

    private List<Map<String, Object>> runQuery(DataSource dataSource, String sql, Map<String, Object> namedParams, List<Object> positionalParams) {
        SqlOperation operation = determineSqlOperation(sql);
        ensureOperationAllowed(operation);

        if (operation == SqlOperation.INSERT || operation == SqlOperation.UPDATE || operation == SqlOperation.DELETE) {
            int affectedRows = runUpdate(dataSource, sql, namedParams, positionalParams);
            return List.of(Map.of("rows_affected", affectedRows));
        }

        if (positionalParams != null && !positionalParams.isEmpty()) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.setMaxRows(100);
            return jdbcTemplate.query(sql, this::mapRowSafely, positionalParams.toArray());
        }

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.getJdbcTemplate().setMaxRows(100);
        return jdbcTemplate.query(sql, namedParams, this::mapRowSafely);
    }

    private int runUpdate(DataSource dataSource, String sql, Map<String, Object> namedParams, List<Object> positionalParams) {
        if (positionalParams != null && !positionalParams.isEmpty()) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.update(sql, positionalParams.toArray());
        }

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.update(sql, namedParams);
    }

    private SqlOperation determineSqlOperation(String sql) {
        if (sql == null) {
            return SqlOperation.OTHER;
        }

        String withoutComments = SQL_LEADING_COMMENTS_PATTERN.matcher(sql).replaceFirst("").trim();
        if (withoutComments.isEmpty()) {
            return SqlOperation.OTHER;
        }

        String firstToken = withoutComments.split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
        return switch (firstToken) {
            case "insert" -> SqlOperation.INSERT;
            case "update" -> SqlOperation.UPDATE;
            case "delete" -> SqlOperation.DELETE;
            default -> SqlOperation.OTHER;
        };
    }

    private void ensureOperationAllowed(SqlOperation operation) {
        if (operation == SqlOperation.INSERT && !canInsert) {
            throw new IllegalArgumentException("INSERT operations are disabled");
        }
        if (operation == SqlOperation.UPDATE && !canUpdate) {
            throw new IllegalArgumentException("UPDATE operations are disabled");
        }
        if (operation == SqlOperation.DELETE && !canDelete) {
            throw new IllegalArgumentException("DELETE operations are disabled");
        }
    }

    private enum SqlOperation {
        INSERT,
        UPDATE,
        DELETE,
        OTHER
    }

    private String rewriteOracleClobEquals(String sql) {
        if (sql == null || sql.isBlank()) {
            return sql;
        }
        if (!sql.toUpperCase(Locale.ROOT).contains("NOTES")) {
            return sql;
        }
        return NOTES_EQUALS_PATTERN.matcher(sql)
                .replaceAll("DBMS_LOB.COMPARE(NOTES, TO_CLOB($1)) = 0");
    }

    private String normalizeOracleTextComparisons(String sql) {
        if (sql == null || sql.isBlank()) {
            return sql;
        }
        return ORACLE_TEXT_LIKE_NUMERIC_LITERAL_PATTERN.matcher(sql)
                .replaceAll("$1 = '$2'");
    }

    private String normalizeSqlForJdbc(String sql) {
        if (sql == null) {
            return null;
        }
        String normalized = sql.trim();
        while (normalized.endsWith(";")) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }
        return normalized;
    }
    
    public List<Map<String, Object>> executeRawSql(DbConnection conn, String sql) {
        return executeRawSql(conn, sql, Collections.emptyMap(), Collections.emptyList());
    }

    public void testConnection(DbConnection conn) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(() -> {
            if (conn.getType() == DbConnection.DbType.LDAP) {
                testLdapConnection(conn);
            } else {
                DataSource dataSource = createDataSource(conn);
                try (Connection c = dataSource.getConnection()) {
                    if (!c.isValid(5)) {
                        throw new RuntimeException("Connection is not valid.");
                    }
                }
            }
            return null;
        });

        try {
            future.get(5, TimeUnit.SECONDS); // 5 seconds timeout
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("Connection timed out after 5 seconds.");
        } finally {
            executor.shutdownNow();
        }
    }

    private void testLdapConnection(DbConnection conn) throws Exception {
        if (conn.getUrl() == null || conn.getUrl().isBlank()) {
            throw new IllegalArgumentException("LDAP URL is required.");
        }
        if (conn.getUsername() == null || conn.getUsername().isBlank()) {
            throw new IllegalArgumentException("LDAP Bind DN is required (use Username field).");
        }
        if (conn.getPassword() == null || conn.getPassword().isBlank()) {
            throw new IllegalArgumentException("LDAP Bind Password is required.");
        }

        Hashtable<String, Object> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, conn.getUrl());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, conn.getUsername());
        env.put(Context.SECURITY_CREDENTIALS, conn.getPassword());

        InitialLdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
    }

    private DataSource createDataSource(DbConnection conn) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(conn.getUrl());
        dataSource.setUsername(conn.getUsername());
        dataSource.setPassword(conn.getPassword());

        switch (conn.getType()) {
            case MYSQL:
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                break;
            case SQLSERVER:
                dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
            case ORACLE:
                dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
                break;
            case LDAP:
                throw new IllegalArgumentException("LDAP connections cannot be used for SQL execution.");
        }
        return dataSource;
    }

    private Map<String, Object> mapRowSafely(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>(columnCount);

        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i);
            String columnTypeName = metaData.getColumnTypeName(i);

            // Oracle JSON columns may require explicit string access when default JSON object mapping is unset.
            if (columnTypeName != null && "JSON".equalsIgnoreCase(columnTypeName)) {
                row.put(columnLabel, rs.getString(i));
            } else {
                Object value = rs.getObject(i);

                if (value instanceof Clob clobValue) {
                    value = clobValue.getSubString(1, (int) clobValue.length());
                } else if (value != null && value.getClass().getName().startsWith("oracle.sql.")) {
                    // Oracle-specific objects (for example oracle.sql.TIMESTAMP) are not Jackson-serializable.
                    value = rs.getString(i);
                }

                row.put(columnLabel, value);
            }
        }

        return row;
    }

    private List<Object> normalizeOraclePositionalParams(List<Object> positionalParams) {
        if (positionalParams == null) {
            return Collections.emptyList();
        }
        return positionalParams.stream()
                .map(this::normalizeOracleParamValue)
                .collect(Collectors.toList());
    }

    private Map<String, Object> normalizeOracleNamedParams(Map<String, Object> namedParams) {
        if (namedParams == null || namedParams.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : namedParams.entrySet()) {
            normalized.put(entry.getKey(), normalizeOracleParamValue(entry.getValue()));
        }
        return normalized;
    }

    private Object normalizeOracleParamValue(Object value) {
        if (!(value instanceof String textValue)) {
            return value;
        }

        String trimmed = textValue.trim();
        if (trimmed.isEmpty()) {
            return value;
        }

        // Common timestamp inputs from UI test fields.
        DateTimeFormatter[] timestampFormats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
        };

        for (DateTimeFormatter formatter : timestampFormats) {
            try {
                LocalDateTime parsed = LocalDateTime.parse(trimmed, formatter);
                return Timestamp.valueOf(parsed);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }

        try {
            LocalDate parsedDate = LocalDate.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE);
            return java.sql.Date.valueOf(parsedDate);
        } catch (DateTimeParseException ignored) {
            return value;
        }
    }

    private List<Object> coerceOraclePositionalStringParams(String sql, List<Object> positionalParams) {
        if (positionalParams == null || positionalParams.isEmpty()) {
            return positionalParams;
        }

        Set<Integer> stringParamIndexes = findTextLikePositionalParamIndexes(sql);
        if (stringParamIndexes.isEmpty()) {
            return positionalParams;
        }

        List<Object> coerced = new ArrayList<>(positionalParams.size());
        for (int i = 0; i < positionalParams.size(); i++) {
            Object value = positionalParams.get(i);
            if (stringParamIndexes.contains(i) && value instanceof Number) {
                coerced.add(String.valueOf(value));
            } else {
                coerced.add(value);
            }
        }
        return coerced;
    }

    private Map<String, Object> coerceOracleNamedStringParams(Map<String, Object> namedParams) {
        if (namedParams == null || namedParams.isEmpty()) {
            return namedParams;
        }

        Map<String, Object> coerced = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : namedParams.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Number && isTextLikeParamName(entry.getKey())) {
                coerced.put(entry.getKey(), String.valueOf(value));
            } else {
                coerced.put(entry.getKey(), value);
            }
        }
        return coerced;
    }

    private Set<Integer> findTextLikePositionalParamIndexes(String sql) {
        if (sql == null || sql.isBlank()) {
            return Collections.emptySet();
        }

        List<Integer> placeholderPositions = findPlaceholderPositions(sql);
        if (placeholderPositions.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Integer> indexes = new HashSet<>();
        var matcher = ORACLE_TEXT_LIKE_BIND_PATTERN.matcher(sql);
        while (matcher.find()) {
            int bindPosition = matcher.end() - 1;
            int placeholderIndex = placeholderPositions.indexOf(bindPosition);
            if (placeholderIndex >= 0) {
                indexes.add(placeholderIndex);
            }
        }
        return indexes;
    }

    private List<Integer> findPlaceholderPositions(String sql) {
        List<Integer> positions = new ArrayList<>();
        boolean inSingleQuote = false;

        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (ch == '\'') {
                if (inSingleQuote && i + 1 < sql.length() && sql.charAt(i + 1) == '\'') {
                    i++;
                    continue;
                }
                inSingleQuote = !inSingleQuote;
                continue;
            }

            if (!inSingleQuote && ch == '?') {
                positions.add(i);
            }
        }

        return positions;
    }

    private boolean isTextLikeParamName(String paramName) {
        if (paramName == null || paramName.isBlank()) {
            return false;
        }
        String lowered = paramName.toLowerCase(Locale.ROOT);
        return ORACLE_STRING_PARAM_HINTS.stream().anyMatch(lowered::contains);
    }

    private String buildOracleInvalidNumberMessage(String sql, Map<String, Object> namedParams, List<Object> positionalParams) {
        return "Oracle ORA-01722 (invalid number): a numeric conversion failed while evaluating your SQL. " +
                "This usually means a NUMBER bind was compared to a VARCHAR2/CHAR column (or vice versa), " +
            "for example username/email/phone filters. Ensure text fields are sent as strings and avoid implicit conversions. " +
                "SQL=" + sql + "; " + summarizeParamTypes(namedParams, positionalParams);
    }

    private String summarizeParamTypes(Map<String, Object> namedParams, List<Object> positionalParams) {
        if (positionalParams != null && !positionalParams.isEmpty()) {
            List<String> typedValues = new ArrayList<>();
            for (Object value : positionalParams) {
                typedValues.add(typeName(value));
            }
            return "positionalParamTypes=" + typedValues;
        }

        if (namedParams != null && !namedParams.isEmpty()) {
            Map<String, String> typedValues = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : namedParams.entrySet()) {
                typedValues.put(entry.getKey(), typeName(entry.getValue()));
            }
            return "namedParamTypes=" + typedValues;
        }

        return "no bind parameters provided";
    }

    private String typeName(Object value) {
        return value == null ? "null" : value.getClass().getSimpleName();
    }
}
