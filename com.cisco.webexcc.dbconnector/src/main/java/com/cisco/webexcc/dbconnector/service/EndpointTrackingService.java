package com.cisco.webexcc.dbconnector.service;

import com.cisco.webexcc.dbconnector.model.EndpointExecution;
import com.cisco.webexcc.dbconnector.model.EndpointStat;
import com.cisco.webexcc.dbconnector.model.EnvironmentStat;
import com.cisco.webexcc.dbconnector.repository.EndpointExecutionRepository;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EndpointTrackingService {

    @Autowired
    private EndpointExecutionRepository repository;

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

    @Transactional
    public void resetEnvironment(String env) {
        // Delete logs for this environment (case insensitive prefix match would be better but we use standard lowercase)
        repository.deleteByEndpointStartingWith("/api/query/" + env.toLowerCase() + "/");
        // Also try uppercase just in case
        repository.deleteByEndpointStartingWith("/api/query/" + env.toUpperCase() + "/");
    }

    @Transactional
    public void cleanupEnvironment(String env) {
        // We want to remove stats for endpoints that are NOT in the configured list.
        // 1. Get all configured names for this env
        var statements = sqlStatementRepository.findByEnvironment(env);
        var configuredNames = statements.stream()
                .map(s -> s.getName().toLowerCase())
                .toList();

        // 2. Get all tracked endpoints for this env (lowercase prefix)
        String prefix = "/api/query/" + env.toLowerCase() + "/";
        var trackedEndpoints = getEndpointCounts(prefix, env); // This now returns union, but we only want to check keys from DB really.
        // Actually getEndpointCounts returns the map. If we iterate the map keys:
        
        for (String endpoint : trackedEndpoints.keySet()) {
            // endpoint format: /api/query/env/name
            String[] parts = endpoint.split("/");
            if (parts.length >= 5) {
                String name = parts[4].toLowerCase();
                if (!configuredNames.contains(name)) {
                    repository.deleteByEndpoint(endpoint);
                }
            } else {
                // Invalid format, delete it
                repository.deleteByEndpoint(endpoint);
            }
        }
    }

    public void trackExecution(String endpoint, int statusCode) {
        EndpointExecution execution = new EndpointExecution(endpoint.toLowerCase(), LocalDateTime.now(), statusCode);
        repository.save(execution);
    }

    public Map<String, Long> getExecutionStats() {
        Map<String, Long> stats = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        stats.put("hour", repository.countByExecutionTimeAfter(now.minusHours(1)));
        stats.put("day", repository.countByExecutionTimeAfter(now.minusDays(1)));
        stats.put("week", repository.countByExecutionTimeAfter(now.minusWeeks(1)));
        stats.put("month", repository.countByExecutionTimeAfter(now.minusMonths(1)));
        stats.put("year", repository.countByExecutionTimeAfter(now.minusYears(1)));

        return stats;
    }

    public Map<String, EnvironmentStat> getEnvironmentStats() {
        Map<String, EnvironmentStat> stats = new LinkedHashMap<>();
        
        // Use lowercase prefixes
        long prodCount = repository.countByEndpointStartingWith("/api/query/prod/");
        long prodFailures = repository.countFailuresByPrefix("/api/query/prod/");
        stats.put("PROD", new EnvironmentStat(prodCount, prodFailures));
        
        long uatCount = repository.countByEndpointStartingWith("/api/query/uat/");
        long uatFailures = repository.countFailuresByPrefix("/api/query/uat/");
        stats.put("UAT", new EnvironmentStat(uatCount, uatFailures));
        
        long devCount = repository.countByEndpointStartingWith("/api/query/dev/");
        long devFailures = repository.countFailuresByPrefix("/api/query/dev/");
        stats.put("DEV", new EnvironmentStat(devCount, devFailures));
        
        return stats;
    }

    public Map<String, Map<String, EndpointStat>> getDetailedEnvironmentStats() {
        Map<String, Map<String, EndpointStat>> stats = new LinkedHashMap<>();
        // Use lowercase prefixes because the frontend generates lowercase links
        stats.put("PROD", getEndpointCounts("/api/query/prod/", "PROD"));
        stats.put("UAT", getEndpointCounts("/api/query/uat/", "UAT"));
        stats.put("DEV", getEndpointCounts("/api/query/dev/", "DEV"));
        return stats;
    }

    private Map<String, EndpointStat> getEndpointCounts(String prefix, String env) {
        Map<String, EndpointStat> map = new HashMap<>();
        
        // 1. Add all configured endpoints with 0 hits
        var statements = sqlStatementRepository.findByEnvironment(env);
        for (var stmt : statements) {
            // Construct the expected endpoint path (lowercase to match frontend links)
            String key = "/api/query/" + env.toLowerCase() + "/" + stmt.getName().toLowerCase();
            map.put(key, new EndpointStat(0L, 0L, null));
        }

        // 2. Overlay with actual execution stats
        java.util.List<Object[]> results = repository.countEndpointsByPrefix(prefix);
        for (Object[] result : results) {
            String endpoint = (String) result[0];
            long count = (Long) result[1];
            LocalDateTime lastExec = (LocalDateTime) result[2];
            
            if (map.containsKey(endpoint)) {
                EndpointStat stat = map.get(endpoint);
                stat.setCount(count);
                stat.setLastExecution(lastExec);
            } else {
                map.put(endpoint, new EndpointStat(count, 0L, lastExec));
            }
        }
        
        // 3. Overlay with failure stats
        java.util.List<Object[]> failureResults = repository.countEndpointFailuresByPrefix(prefix);
        for (Object[] result : failureResults) {
            String endpoint = (String) result[0];
            long failedCount = (Long) result[1];
            
            if (map.containsKey(endpoint)) {
                map.get(endpoint).setFailedCount(failedCount);
            }
            // If it's not in the map, it means it wasn't in step 2 (so 0 total hits?), which is impossible if it has failures.
            // Or it was in step 2, so it should be in the map.
        }
        
        return map;
    }
}
