package com.cisco.webexcc.dbconnector.controller;

import com.cisco.webexcc.dbconnector.service.SqlExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private SqlExecutionService sqlExecutionService;

    @GetMapping("/query/{env}/{name}")
    public ResponseEntity<?> executeQuery(@PathVariable String env, @PathVariable String name, @RequestParam Map<String, String> queryParams) {
        logger.info("Received query request for env: {}, name: {}, params: {}", env, name, queryParams);
        try {
            // Convert Map<String, String> to Map<String, Object>
            Map<String, Object> params = queryParams.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            List<Map<String, Object>> result = sqlExecutionService.executeSql(name, env.toUpperCase(), params);
            logger.debug("Query executed successfully, returning {} rows", result.size());
            
            if (result.isEmpty()) {
                return ResponseEntity.ok(Map.of());
            } else if (result.size() == 1) {
                return ResponseEntity.ok(result.get(0));
            } else {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            logger.error("Error executing query for env: {}, name: {}", env, name, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
