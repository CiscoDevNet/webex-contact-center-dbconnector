package com.cisco.webexcc.dbconnector.service;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.SqlStatement;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class SqlExecutionService {

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

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
        DataSource dataSource = createDataSource(conn);
        
        if (positionalParams != null && !positionalParams.isEmpty()) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.setMaxRows(100);
            return jdbcTemplate.queryForList(sql, positionalParams.toArray());
        } else {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            jdbcTemplate.getJdbcTemplate().setMaxRows(100); 
            return jdbcTemplate.queryForList(sql, params);
        }
    }
    
    public List<Map<String, Object>> executeRawSql(DbConnection conn, String sql) {
        return executeRawSql(conn, sql, Collections.emptyMap(), Collections.emptyList());
    }

    public void testConnection(DbConnection conn) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(() -> {
            DataSource dataSource = createDataSource(conn);
            try (Connection c = dataSource.getConnection()) {
                if (!c.isValid(5)) {
                    throw new RuntimeException("Connection is not valid.");
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
        }
        return dataSource;
    }
}
