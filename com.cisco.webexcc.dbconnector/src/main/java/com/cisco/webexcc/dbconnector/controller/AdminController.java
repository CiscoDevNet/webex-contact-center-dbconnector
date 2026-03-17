package com.cisco.webexcc.dbconnector.controller;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.LdapStatement;
import com.cisco.webexcc.dbconnector.model.SqlStatement;
import com.cisco.webexcc.dbconnector.repository.DbConnectionRepository;
import com.cisco.webexcc.dbconnector.repository.LdapStatementRepository;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import com.cisco.webexcc.dbconnector.ldap.LdapQueryService;
import com.cisco.webexcc.dbconnector.service.SqlExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private DbConnectionRepository dbConnectionRepository;

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

    @Autowired
    private LdapStatementRepository ldapStatementRepository;

    @Autowired
    private SqlExecutionService sqlExecutionService;

    @Autowired
    private LdapQueryService ldapQueryService;

    // --- Connections ---

    @GetMapping("/connections")
    public String listConnections(Model model) {
        List<DbConnection> connections = dbConnectionRepository.findAll();
        for (DbConnection conn : connections) {
            conn.setEndpointCount(sqlStatementRepository.countByDbConnection(conn));
        }
        model.addAttribute("connections", connections);
        return "admin/connections";
    }

    @GetMapping("/connections/add")
    public String addConnectionForm(Model model) {
        model.addAttribute("connection", new DbConnection());
        return "admin/connection-form";
    }

    @GetMapping("/connections/edit/{id}")
    public String editConnectionForm(@PathVariable UUID id, Model model) {
        DbConnection connection = dbConnectionRepository.findById(id).orElseThrow();
        model.addAttribute("connection", connection);
        return "admin/connection-form";
    }

    @PostMapping("/connections/save")
    public String saveConnection(@ModelAttribute DbConnection connection, RedirectAttributes redirectAttributes) {
        try {
            dbConnectionRepository.save(connection);
            redirectAttributes.addFlashAttribute("successMessage", "Connection saved successfully.");
        } catch (Exception ex) {
            logger.error("Failed to save connection {}", connection.getName(), ex);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save connection: " + ex.getMessage());
            return "redirect:/admin/connections/add";
        }
        return "redirect:/admin/connections";
    }

    @GetMapping("/connections/delete/{id}")
    public String deleteConnection(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        DbConnection connection = dbConnectionRepository.findById(id).orElse(null);
        if (connection == null) {
             redirectAttributes.addFlashAttribute("errorMessage", "Connection not found.");
             return "redirect:/admin/connections";
        }

        List<SqlStatement> dependencies = sqlStatementRepository.findByDbConnection(connection);
        List<LdapStatement> ldapDependencies = ldapStatementRepository.findByDbConnection(connection);
        if (!dependencies.isEmpty() || !ldapDependencies.isEmpty()) {
            StringBuilder sb = new StringBuilder("Cannot delete connection. It is used by endpoints: ");
            for (int i = 0; i < dependencies.size(); i++) {
                sb.append("SQL:").append(dependencies.get(i).getName());
                if (i < dependencies.size() - 1 || !ldapDependencies.isEmpty()) {
                    sb.append(", ");
                }
            }
            for (int i = 0; i < ldapDependencies.size(); i++) {
                sb.append("LDAP:").append(ldapDependencies.get(i).getName());
                if (i < ldapDependencies.size() - 1) {
                    sb.append(", ");
                }
            }
            redirectAttributes.addFlashAttribute("errorMessage", sb.toString());
            return "redirect:/admin/connections";
        }

        dbConnectionRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Connection deleted successfully.");
        return "redirect:/admin/connections";
    }

    @GetMapping("/connections/test/{id}")
    public String testConnection(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        DbConnection connection = dbConnectionRepository.findById(id).orElseThrow();
        try {
            sqlExecutionService.testConnection(connection);
            redirectAttributes.addFlashAttribute("successMessage", "Connection successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Connection failed: " + e.getMessage());
        }
        return "redirect:/admin/connections";
    }

    @PostMapping("/connections/test")
    @ResponseBody
    public Map<String, Object> testConnectionAjax(@RequestBody DbConnection connection) {
        System.out.println("Testing connection: " + connection.getName() + " (" + connection.getUrl() + ")");
        Map<String, Object> response = new HashMap<>();
        try {
            sqlExecutionService.testConnection(connection);
            System.out.println("Connection successful");
            response.put("success", true);
            response.put("message", "Connection successful!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Connection failed: " + e.getMessage());
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            response.put("stacktrace", sw.toString());
        }
        return response;
    }

    @PostMapping("/sql/test-ajax")
    @ResponseBody
    public Map<String, Object> testSqlAjax(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String sql = (String) payload.get("sql");
            String connectionIdStr = (String) payload.get("connectionId");
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) payload.getOrDefault("params", new HashMap<>());
            @SuppressWarnings("unchecked")
            List<Object> positionalParams = (List<Object>) payload.getOrDefault("positionalParams", new ArrayList<>());
            
            if (connectionIdStr == null || connectionIdStr.isEmpty()) {
                throw new IllegalArgumentException("Connection ID is required");
            }
            
            UUID connectionId = UUID.fromString(connectionIdStr);
            
            DbConnection connection = dbConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

            var results = sqlExecutionService.executeRawSql(connection, sql, params, positionalParams);
            
            response.put("status", "success");
            if (results.isEmpty()) {
                response.put("data", new HashMap<>());
            } else if (results.size() == 1) {
                response.put("data", results.get(0));
            } else {
                response.put("data", results);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Execution failed: " + e.getMessage());
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            response.put("stacktrace", sw.toString());
        }
        return response;
    }

    // --- SQL Statements ---

    @GetMapping("/sql")
    public String listSql(Model model) {
        model.addAttribute("statements", sqlStatementRepository.findAll());
        model.addAttribute("hasConnections", !getDevSqlConnections().isEmpty());
        return "admin/sql-statements";
    }

    @GetMapping("/sql/add")
    public String addSqlForm(Model model) {
        model.addAttribute("statement", new SqlStatement());
        model.addAttribute("connections", getDevSqlConnections());
        return "admin/sql-form";
    }

    @GetMapping("/sql/edit/{id}")
    public String editSqlForm(@PathVariable UUID id, Model model) {
        SqlStatement statement = sqlStatementRepository.findById(id).orElseThrow();
        model.addAttribute("statement", statement);
        model.addAttribute("connections", getDevSqlConnections());
        return "admin/sql-form";
    }

    @PostMapping("/sql/save")
    public String saveSql(@ModelAttribute("statement") SqlStatement statement, Model model, RedirectAttributes redirectAttributes) {
        // Check for duplicate name in the same environment
        java.util.Optional<SqlStatement> existing = sqlStatementRepository.findByNameIgnoreCaseAndEnvironment(statement.getName(), statement.getEnvironment());
        
        if (existing.isPresent() && !existing.get().getId().equals(statement.getId())) {
            model.addAttribute("errorMessage", "An endpoint with the name '" + statement.getName() + "' already exists in " + statement.getEnvironment() + ". Please choose a different name.");
            model.addAttribute("connections", getDevSqlConnections());
            return "admin/sql-form";
        }

        sqlStatementRepository.save(statement);
        redirectAttributes.addFlashAttribute("successMessage", "SQL Statement saved successfully.");
        return "redirect:/admin/sql";
    }

    @GetMapping("/sql/delete/{id}")
    public String deleteSql(@PathVariable UUID id) {
        sqlStatementRepository.deleteById(id);
        return "redirect:/admin/sql";
    }

    @GetMapping("/sql/deploy/{id}")
    public String deploySqlForm(@PathVariable UUID id, Model model) {
        SqlStatement source = sqlStatementRepository.findById(id).orElseThrow();
        model.addAttribute("source", source);
        model.addAttribute("connections", getDeployableSqlConnections());
        model.addAttribute("requiresConfirmation", false);
        return "admin/deploy-form";
    }

    @PostMapping("/sql/deploy")
    public String deploySql(@RequestParam UUID sourceId, 
                            @RequestParam UUID connectionId, 
                            @RequestParam(required = false, defaultValue = "false") boolean confirmed,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        SqlStatement source = sqlStatementRepository.findById(sourceId).orElseThrow();
        DbConnection targetConn = dbConnectionRepository.findById(connectionId).orElseThrow();

        if (targetConn.getType() == DbConnection.DbType.LDAP) {
            model.addAttribute("source", source);
            model.addAttribute("connections", getDeployableSqlConnections());
            model.addAttribute("selectedConnectionId", connectionId);
            model.addAttribute("warningMessage", "Only SQL database connections can be used for SQL deployment.");
            model.addAttribute("requiresConfirmation", false);
            return "admin/deploy-form";
        }

        String targetEnv = targetConn.getEnvironment();

        // Check if version already exists in target environment
        java.util.Optional<SqlStatement> existingTarget = sqlStatementRepository.findByNameIgnoreCaseAndEnvironment(source.getName(), targetEnv);

        if (existingTarget.isPresent() && !confirmed) {
            model.addAttribute("source", source);
            model.addAttribute("connections", getDeployableSqlConnections());
            model.addAttribute("selectedConnectionId", connectionId);
            model.addAttribute("warningMessage", "Endpoint '" + source.getName() + "' already exists in " + targetEnv + ". Do you want to overwrite it?");
            model.addAttribute("requiresConfirmation", true);
            return "admin/deploy-form";
        }

        SqlStatement target = existingTarget.orElse(new SqlStatement());

        boolean isNew = target.getId() == null;

        target.setName(source.getName());
        target.setSqlContent(source.getSqlContent());
        target.setParamNames(source.getParamNames());
        target.setEnvironment(targetEnv);
        target.setDbConnection(targetConn);
        
        sqlStatementRepository.save(target);
        
        String message = isNew ? "Deployed new endpoint to " : "Updated endpoint in ";
        redirectAttributes.addFlashAttribute("successMessage", message + targetEnv + ": " + source.getName());
        
        return "redirect:/admin/sql";
    }

    private List<DbConnection> getDeployableSqlConnections() {
        return dbConnectionRepository.findByEnvironmentNotAndTypeNot("DEV", DbConnection.DbType.LDAP);
    }

    private List<DbConnection> getDevSqlConnections() {
        return dbConnectionRepository.findByEnvironment("DEV").stream()
                .filter(Objects::nonNull)
                .filter(conn -> conn.getType() != DbConnection.DbType.LDAP)
                .toList();
    }

    // --- Testing ---

    @GetMapping("/test")
    public String testPage(Model model) {
        List<TestEndpointView> statements = new ArrayList<>();

        for (SqlStatement stmt : sqlStatementRepository.findAll()) {
            statements.add(new TestEndpointView(
                    stmt.getName(),
                    stmt.getEnvironment(),
                    stmt.getParamNames(),
                    stmt.getDbConnection() != null && stmt.getDbConnection().getType() != null
                            ? stmt.getDbConnection().getType().name()
                            : "SQL",
                    "SQL",
                    "/api/query/" + stmt.getEnvironment().toLowerCase() + "/" + stmt.getName().toLowerCase()
            ));
        }

        for (LdapStatement stmt : ldapStatementRepository.findAll()) {
            statements.add(new TestEndpointView(
                    stmt.getName(),
                    stmt.getEnvironment(),
                    stmt.getParamNames(),
                    stmt.getDbConnection() != null && stmt.getDbConnection().getType() != null
                            ? stmt.getDbConnection().getType().name()
                            : "LDAP",
                    "LDAP",
                    "/api/ldap/query/" + stmt.getEnvironment().toLowerCase() + "/" + stmt.getName().toLowerCase()
            ));
        }

        statements.sort(Comparator
                .comparing(TestEndpointView::getEnvironment, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(TestEndpointView::getType, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(TestEndpointView::getName, String.CASE_INSENSITIVE_ORDER));

        model.addAttribute("statements", statements);
        return "admin/test-page";
    }

    public static class TestEndpointView {
        private final String name;
        private final String environment;
        private final String paramNames;
        private final String connectionType;
        private final String type;
        private final String apiPath;

        public TestEndpointView(String name, String environment, String paramNames, String connectionType, String type, String apiPath) {
            this.name = name;
            this.environment = environment;
            this.paramNames = paramNames;
            this.connectionType = connectionType;
            this.type = type;
            this.apiPath = apiPath;
        }

        public String getName() {
            return name;
        }

        public String getEnvironment() {
            return environment;
        }

        public String getParamNames() {
            return paramNames;
        }

        public String getConnectionType() {
            return connectionType;
        }

        public String getType() {
            return type;
        }

        public String getApiPath() {
            return apiPath;
        }
    }

    // --- LDAP Statements ---

    @GetMapping("/ldap")
    public String listLdap(Model model) {
        model.addAttribute("statements", ldapStatementRepository.findAll());
        model.addAttribute("hasConnections", !getAllLdapConnections().isEmpty());
        return "admin/ldap-statements";
    }

    @GetMapping("/ldap/add")
    public String addLdapForm(Model model) {
        model.addAttribute("statement", new LdapStatement());
        model.addAttribute("connections", getAllLdapConnections());
        return "admin/ldap-form";
    }

    @GetMapping("/ldap/edit/{id}")
    public String editLdapForm(@PathVariable UUID id, Model model) {
        LdapStatement statement = ldapStatementRepository.findById(id).orElseThrow();
        model.addAttribute("statement", statement);
        model.addAttribute("connections", getAllLdapConnections());
        return "admin/ldap-form";
    }

    @PostMapping("/ldap/save")
    public String saveLdap(@ModelAttribute("statement") LdapStatement statement, Model model, RedirectAttributes redirectAttributes) {
        if (statement.getDbConnection() == null || statement.getDbConnection().getId() == null) {
            model.addAttribute("errorMessage", "Please select an LDAP connection.");
            model.addAttribute("connections", getAllLdapConnections());
            return "admin/ldap-form";
        }

        DbConnection selectedConnection = dbConnectionRepository.findById(statement.getDbConnection().getId()).orElse(null);
        if (selectedConnection == null) {
            model.addAttribute("errorMessage", "Selected connection was not found.");
            model.addAttribute("connections", getAllLdapConnections());
            return "admin/ldap-form";
        }

        if (selectedConnection.getType() != DbConnection.DbType.LDAP) {
            model.addAttribute("errorMessage", "Selected connection must be of type LDAP.");
            model.addAttribute("connections", getAllLdapConnections());
            return "admin/ldap-form";
        }

        statement.setDbConnection(selectedConnection);
        statement.setEnvironment(selectedConnection.getEnvironment());

        java.util.Optional<LdapStatement> existing = ldapStatementRepository.findByNameIgnoreCaseAndEnvironment(statement.getName(), statement.getEnvironment());

        if (existing.isPresent() && !existing.get().getId().equals(statement.getId())) {
            model.addAttribute("errorMessage", "An LDAP endpoint with the name '" + statement.getName() + "' already exists in " + statement.getEnvironment() + ". Please choose a different name.");
            model.addAttribute("connections", getAllLdapConnections());
            return "admin/ldap-form";
        }

        ldapStatementRepository.save(statement);
        redirectAttributes.addFlashAttribute("successMessage", "LDAP Statement saved successfully.");
        return "redirect:/admin/ldap";
    }

    @GetMapping("/ldap/delete/{id}")
    public String deleteLdap(@PathVariable UUID id) {
        ldapStatementRepository.deleteById(id);
        return "redirect:/admin/ldap";
    }

    @GetMapping({"/ldap/deploy/{id}", "/ldap/deploy/{id}/"})
    public String deployLdapForm(@PathVariable UUID id, Model model) {
        LdapStatement source = ldapStatementRepository.findById(id).orElseThrow();
        model.addAttribute("source", source);
        model.addAttribute("connections", getNonDevLdapConnections());
        model.addAttribute("requiresConfirmation", false);
        return "admin/ldap-deploy-form";
    }

    @GetMapping("/ldap/deploy")
    public String deployLdapFormByQuery(@RequestParam UUID sourceId, Model model) {
        return deployLdapForm(sourceId, model);
    }

    @PostMapping({"/ldap/deploy", "/ldap/deploy/"})
    public String deployLdap(@RequestParam UUID sourceId,
                             @RequestParam UUID connectionId,
                             @RequestParam(required = false, defaultValue = "false") boolean confirmed,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        LdapStatement source = ldapStatementRepository.findById(sourceId).orElseThrow();
        DbConnection targetConn = dbConnectionRepository.findById(connectionId).orElseThrow();

        if (targetConn.getType() != DbConnection.DbType.LDAP) {
            model.addAttribute("source", source);
            model.addAttribute("connections", getNonDevLdapConnections());
            model.addAttribute("errorMessage", "Selected connection must be LDAP.");
            model.addAttribute("requiresConfirmation", false);
            return "admin/ldap-deploy-form";
        }

        String targetEnv = targetConn.getEnvironment();

        java.util.Optional<LdapStatement> existingTarget = ldapStatementRepository
                .findByNameIgnoreCaseAndEnvironment(source.getName(), targetEnv);

        if (existingTarget.isPresent() && !confirmed) {
            model.addAttribute("source", source);
            model.addAttribute("connections", getNonDevLdapConnections());
            model.addAttribute("selectedConnectionId", connectionId);
            model.addAttribute("warningMessage", "LDAP endpoint '" + source.getName() + "' already exists in " + targetEnv + ". Do you want to overwrite it?");
            model.addAttribute("requiresConfirmation", true);
            return "admin/ldap-deploy-form";
        }

        LdapStatement target = existingTarget.orElse(new LdapStatement());
        boolean isNew = target.getId() == null;

        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setBaseDn(source.getBaseDn());
        target.setFilterContent(source.getFilterContent());
        target.setAttributes(source.getAttributes());
        target.setParamNames(source.getParamNames());
        target.setEnvironment(targetEnv);
        target.setDbConnection(targetConn);

        ldapStatementRepository.save(target);

        String message = isNew ? "Deployed new LDAP endpoint to " : "Updated LDAP endpoint in ";
        redirectAttributes.addFlashAttribute("successMessage", message + targetEnv + ": " + source.getName());

        return "redirect:/admin/ldap";
    }

    private List<DbConnection> getAllLdapConnections() {
        return dbConnectionRepository.findAll().stream()
                .filter(Objects::nonNull)
                .filter(conn -> conn.getType() == DbConnection.DbType.LDAP)
                .sorted(Comparator
                        .comparing(DbConnection::getEnvironment, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(DbConnection::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

        private List<DbConnection> getNonDevLdapConnections() {
        return getAllLdapConnections().stream()
            .filter(conn -> !"DEV".equalsIgnoreCase(conn.getEnvironment()))
            .toList();
        }

    @PostMapping("/ldap/test-ajax")
    @ResponseBody
    public Map<String, Object> testLdapAjax(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String connectionIdStr = (String) payload.get("connectionId");
            String baseDn = (String) payload.get("baseDn");
            String filter = (String) payload.get("filter");
            String attributesCsv = (String) payload.get("attributes");
            @SuppressWarnings("unchecked")
            List<String> paramNames = (List<String>) payload.getOrDefault("paramNames", new ArrayList<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> paramValues = (Map<String, Object>) payload.getOrDefault("paramValues", new HashMap<>());

            if (connectionIdStr == null || connectionIdStr.isEmpty()) {
                throw new IllegalArgumentException("Connection ID is required");
            }

            UUID connectionId = UUID.fromString(connectionIdStr);
            DbConnection connection = dbConnectionRepository.findById(connectionId)
                    .orElseThrow(() -> new RuntimeException("Connection not found"));

            if (connection.getType() != DbConnection.DbType.LDAP) {
                throw new IllegalArgumentException("Selected connection must be LDAP");
            }

            List<String> args = new ArrayList<>();
            for (String name : paramNames) {
                Object value = paramValues.get(name);
                args.add(value == null ? "" : String.valueOf(value));
            }

            List<String> attrs = new ArrayList<>();
            if (attributesCsv != null && !attributesCsv.isBlank()) {
                for (String token : attributesCsv.split(",")) {
                    String trimmed = token.trim();
                    if (!trimmed.isEmpty()) {
                        attrs.add(trimmed);
                    }
                }
            }

            List<Map<String, Object>> results = ldapQueryService.search(
                    connection.getUrl(),
                    connection.getUsername(),
                    connection.getPassword(),
                    baseDn,
                    filter,
                    args,
                    attrs,
                    100,
                    5000,
                    "subtree"
            );

            response.put("status", "success");
            if (results.isEmpty()) {
                response.put("data", new HashMap<>());
            } else if (results.size() == 1) {
                response.put("data", results.get(0));
            } else {
                response.put("data", results);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Execution failed: " + e.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            response.put("stacktrace", sw.toString());
        }
        return response;
    }
}
