package com.cisco.webexcc.dbconnector.controller;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.SqlStatement;
import com.cisco.webexcc.dbconnector.repository.DbConnectionRepository;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import com.cisco.webexcc.dbconnector.service.SqlExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DbConnectionRepository dbConnectionRepository;

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

    @Autowired
    private SqlExecutionService sqlExecutionService;

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
    public String saveConnection(@ModelAttribute DbConnection connection) {
        dbConnectionRepository.save(connection);
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
        if (!dependencies.isEmpty()) {
            StringBuilder sb = new StringBuilder("Cannot delete connection. It is used by the following SQL endpoints: ");
            for (int i = 0; i < dependencies.size(); i++) {
                sb.append(dependencies.get(i).getName());
                if (i < dependencies.size() - 1) {
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
        model.addAttribute("hasConnections", !dbConnectionRepository.findByEnvironment("DEV").isEmpty());
        return "admin/sql-statements";
    }

    @GetMapping("/sql/add")
    public String addSqlForm(Model model) {
        model.addAttribute("statement", new SqlStatement());
        model.addAttribute("connections", dbConnectionRepository.findByEnvironment("DEV"));
        return "admin/sql-form";
    }

    @GetMapping("/sql/edit/{id}")
    public String editSqlForm(@PathVariable UUID id, Model model) {
        SqlStatement statement = sqlStatementRepository.findById(id).orElseThrow();
        model.addAttribute("statement", statement);
        model.addAttribute("connections", dbConnectionRepository.findByEnvironment("DEV"));
        return "admin/sql-form";
    }

    @PostMapping("/sql/save")
    public String saveSql(@ModelAttribute("statement") SqlStatement statement, Model model, RedirectAttributes redirectAttributes) {
        // Check for duplicate name in the same environment
        java.util.Optional<SqlStatement> existing = sqlStatementRepository.findByNameIgnoreCaseAndEnvironment(statement.getName(), statement.getEnvironment());
        
        if (existing.isPresent() && !existing.get().getId().equals(statement.getId())) {
            model.addAttribute("errorMessage", "An endpoint with the name '" + statement.getName() + "' already exists in " + statement.getEnvironment() + ". Please choose a different name.");
            model.addAttribute("connections", dbConnectionRepository.findByEnvironment("DEV"));
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
        model.addAttribute("connections", dbConnectionRepository.findByEnvironmentNot("DEV"));
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
        String targetEnv = targetConn.getEnvironment();

        // Check if version already exists in target environment
        java.util.Optional<SqlStatement> existingTarget = sqlStatementRepository.findByNameIgnoreCaseAndEnvironment(source.getName(), targetEnv);

        if (existingTarget.isPresent() && !confirmed) {
            model.addAttribute("source", source);
            model.addAttribute("connections", dbConnectionRepository.findByEnvironmentNot("DEV"));
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

    // --- Testing ---

    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("statements", sqlStatementRepository.findAll());
        return "admin/test-page";
    }
}
