package com.cisco.webexcc.dbconnector.ldap;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.LdapStatement;
import com.cisco.webexcc.dbconnector.repository.DbConnectionRepository;
import com.cisco.webexcc.dbconnector.repository.LdapStatementRepository;
import com.cisco.webexcc.dbconnector.util.LogSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.naming.NamingException;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/ldap")
public class LdapController {

    private static final Logger logger = LoggerFactory.getLogger(LdapController.class);

    private final LdapQueryService ldapQueryService;
    private final LdapProperties ldapProperties;
    private final DbConnectionRepository dbConnectionRepository;
    private final LdapStatementRepository ldapStatementRepository;

    public LdapController(
            LdapQueryService ldapQueryService,
            LdapProperties ldapProperties,
            DbConnectionRepository dbConnectionRepository,
            LdapStatementRepository ldapStatementRepository
    ) {
        this.ldapQueryService = ldapQueryService;
        this.ldapProperties = ldapProperties;
        this.dbConnectionRepository = dbConnectionRepository;
        this.ldapStatementRepository = ldapStatementRepository;
    }

    @RequestMapping(value = "/query/{env}/{name}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> executeSavedQuery(
            @PathVariable String env,
            @PathVariable String name,
            @RequestParam Map<String, String> queryParams,
            @RequestBody(required = false) Map<String, Object> bodyParams
    ) {
        try {
            String normalizedEnv = env.toUpperCase();
            LdapStatement statement = ldapStatementRepository.findByNameIgnoreCaseAndEnvironment(name, normalizedEnv)
                    .orElseThrow(() -> new IllegalArgumentException("LDAP Statement not found: " + name + " in " + normalizedEnv));

            DbConnection connection = statement.getDbConnection();
            if (connection == null || connection.getType() != DbConnection.DbType.LDAP) {
                throw new IllegalArgumentException("LDAP statement is not bound to a valid LDAP connection");
            }

            Map<String, Object> merged = new java.util.HashMap<>();
            merged.putAll(queryParams);
            if (bodyParams != null) {
                merged.putAll(bodyParams);
            }

            List<String> args = new ArrayList<>();
            if (statement.getParamNames() != null && !statement.getParamNames().isBlank()) {
                String[] names = statement.getParamNames().split(",");
                for (String token : names) {
                    String key = token.trim();
                    if (!key.isEmpty()) {
                        Object value = merged.get(key);
                        args.add(value == null ? "" : String.valueOf(value));
                    }
                }
            }

            List<String> attrs = new ArrayList<>();
            if (statement.getAttributes() != null && !statement.getAttributes().isBlank()) {
                String[] names = statement.getAttributes().split(",");
                for (String token : names) {
                    String attr = token.trim();
                    if (!attr.isEmpty()) {
                        attrs.add(attr);
                    }
                }
            }

            List<Map<String, Object>> data = ldapQueryService.search(
                    connection.getUrl(),
                    connection.getUsername(),
                    connection.getPassword(),
                    statement.getBaseDn(),
                    statement.getFilterContent(),
                    args,
                    attrs,
                    ldapProperties.getSizeLimit(),
                    ldapProperties.getTimeLimitMs(),
                    ldapProperties.getScope()
            );

            if (data.isEmpty()) {
                return ResponseEntity.ok(Map.of());
            } else if (data.size() == 1) {
                return ResponseEntity.ok(data.get(0));
            }
            return ResponseEntity.ok(data);
        } catch (Exception ex) {
            logger.error("Saved LDAP query execution failed: {}", LogSanitizer.sanitize(ex));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", ex.getMessage()));
        }
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> search(
            @RequestParam(required = false) UUID connectionId,
            @RequestParam(required = false) String connectionName,
            @RequestParam(required = false) String env,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String bindDn,
            @RequestParam(required = false) String bindPassword,
            @RequestParam(required = false) String baseDn,
            @RequestParam(required = false) String filter,
            @RequestParam(name = "arg", required = false) List<String> filterArgs,
            @RequestParam(name = "attr", required = false) List<String> attributes,
            @RequestParam(required = false) Integer sizeLimit,
            @RequestParam(required = false) Integer timeLimitMs,
            @RequestParam(required = false) String scope,
            @RequestBody(required = false) LdapSearchRequest body
    ) {
        DbConnection resolvedConnection = resolveConnection(connectionId, connectionName, env, body);

        String connectionUrl = resolvedConnection != null ? resolvedConnection.getUrl() : null;
        String connectionBindDn = resolvedConnection != null ? resolvedConnection.getUsername() : null;
        String connectionBindPassword = resolvedConnection != null ? resolvedConnection.getPassword() : null;

        String effectiveUrl = firstNonBlank(url, body != null ? body.getUrl() : null, connectionUrl, ldapProperties.getUrl());
        String effectiveBindDn = firstNonBlank(bindDn, body != null ? body.getBindDn() : null, connectionBindDn, ldapProperties.getBindDn());
        String effectiveBindPassword = firstNonBlank(bindPassword, body != null ? body.getBindPassword() : null, connectionBindPassword, ldapProperties.getBindPassword());
        String effectiveBaseDn = firstNonBlank(baseDn, body != null ? body.getBaseDn() : null, ldapProperties.getBaseDn(), null);
        String effectiveFilter = firstNonBlank(filter, body != null ? body.getFilter() : null, ldapProperties.getFilter(), null);
        String effectiveScope = firstNonBlank(scope, body != null ? body.getScope() : null, ldapProperties.getScope(), null);
        int effectiveSizeLimit = sizeLimit != null ? sizeLimit : body != null && body.getSizeLimit() != null ? body.getSizeLimit() : ldapProperties.getSizeLimit();
        int effectiveTimeLimitMs = timeLimitMs != null ? timeLimitMs : body != null && body.getTimeLimitMs() != null ? body.getTimeLimitMs() : ldapProperties.getTimeLimitMs();
        List<String> effectiveFilterArgs = filterArgs != null ? filterArgs : body != null ? body.getArgs() : null;
        List<String> effectiveAttributes = attributes != null ? attributes : body != null ? body.getAttrs() : null;

        if (effectiveFilterArgs == null) {
            effectiveFilterArgs = new ArrayList<>();
        }
        if (effectiveAttributes == null) {
            effectiveAttributes = new ArrayList<>();
        }

        logger.info(
                "LDAP search request baseDn={}, filter={}, args={}, attrs={}, scope={}",
            LogSanitizer.sanitize(effectiveBaseDn),
                effectiveFilter,
            LogSanitizer.maskValues(effectiveFilterArgs),
                effectiveAttributes,
                effectiveScope
        );

        try {
            List<Map<String, Object>> data = ldapQueryService.search(
                    effectiveUrl,
                    effectiveBindDn,
                    effectiveBindPassword,
                    effectiveBaseDn,
                    effectiveFilter,
                    effectiveFilterArgs,
                    effectiveAttributes,
                    effectiveSizeLimit,
                    effectiveTimeLimitMs,
                    effectiveScope
            );
            return ResponseEntity.ok(Map.of("count", data.size(), "results", data));
        } catch (IllegalArgumentException ex) {
            logger.error("LDAP request invalid: {}", LogSanitizer.sanitize(ex));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", ex.getMessage()));
        } catch (NamingException ex) {
            logger.error("LDAP search failed: {}", LogSanitizer.sanitize(ex));
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("status", "error", "message", ex.getMessage()));
        }
    }

    private DbConnection resolveConnection(UUID connectionId, String connectionName, String env, LdapSearchRequest body) {
        UUID effectiveConnectionId = connectionId != null ? connectionId : body != null ? body.getConnectionId() : null;
        String effectiveConnectionName = firstNonBlank(connectionName, body != null ? body.getConnectionName() : null, null, null);
        String effectiveEnv = firstNonBlank(env, body != null ? body.getEnv() : null, "DEV", null);

        if (effectiveConnectionId == null && (effectiveConnectionName == null || effectiveConnectionName.isBlank())) {
            return null;
        }

        DbConnection connection;
        if (effectiveConnectionId != null) {
            connection = dbConnectionRepository.findById(effectiveConnectionId)
                    .orElseThrow(() -> new IllegalArgumentException("Connection not found for id: " + effectiveConnectionId));
        } else {
            connection = dbConnectionRepository.findByNameIgnoreCaseAndEnvironment(effectiveConnectionName, effectiveEnv.toUpperCase())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Connection not found for name '" + effectiveConnectionName + "' in env '" + effectiveEnv.toUpperCase() + "'"
                    ));
        }

        if (connection.getType() != DbConnection.DbType.LDAP) {
            throw new IllegalArgumentException("Selected connection must be of type LDAP.");
        }

        return connection;
    }

    private String firstNonBlank(String primary, String secondary, String tertiary, String fallback) {
        if (primary != null && !primary.isBlank()) {
            return primary;
        }
        if (secondary != null && !secondary.isBlank()) {
            return secondary;
        }
        if (tertiary != null && !tertiary.isBlank()) {
            return tertiary;
        }
        return fallback;
    }

    public static class LdapSearchRequest {
        private UUID connectionId;
        private String connectionName;
        private String env;
        private String url;
        private String bindDn;
        private String bindPassword;
        private String baseDn;
        private String filter;
        private List<String> args;
        private List<String> attrs;
        private Integer sizeLimit;
        private Integer timeLimitMs;
        private String scope;

        public UUID getConnectionId() {
            return connectionId;
        }

        public void setConnectionId(UUID connectionId) {
            this.connectionId = connectionId;
        }

        public String getConnectionName() {
            return connectionName;
        }

        public void setConnectionName(String connectionName) {
            this.connectionName = connectionName;
        }

        public String getEnv() {
            return env;
        }

        public void setEnv(String env) {
            this.env = env;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBindDn() {
            return bindDn;
        }

        public void setBindDn(String bindDn) {
            this.bindDn = bindDn;
        }

        public String getBindPassword() {
            return bindPassword;
        }

        public void setBindPassword(String bindPassword) {
            this.bindPassword = bindPassword;
        }

        public String getBaseDn() {
            return baseDn;
        }

        public void setBaseDn(String baseDn) {
            this.baseDn = baseDn;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public List<String> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<String> attrs) {
            this.attrs = attrs;
        }

        public Integer getSizeLimit() {
            return sizeLimit;
        }

        public void setSizeLimit(Integer sizeLimit) {
            this.sizeLimit = sizeLimit;
        }

        public Integer getTimeLimitMs() {
            return timeLimitMs;
        }

        public void setTimeLimitMs(Integer timeLimitMs) {
            this.timeLimitMs = timeLimitMs;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}