package com.cisco.webexcc.dbconnector.controller;

import com.cisco.webexcc.dbconnector.service.SqlExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private SqlExecutionService sqlExecutionService;

    @RequestMapping(value = "/query/{env}/{name}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> executeQuery(
            @PathVariable String env,
            @PathVariable String name,
            @RequestParam Map<String, String> queryParams,
            @RequestBody(required = false) Map<String, Object> bodyParams
    ) {
        logger.info("Received query request for env: {}, name: {}, queryParams: {}, hasBody: {}", env, name, queryParams, bodyParams != null);
        try {
            Map<String, Object> params = new HashMap<>();
            params.putAll(queryParams);
            if (bodyParams != null) {
                params.putAll(bodyParams);
            }
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
