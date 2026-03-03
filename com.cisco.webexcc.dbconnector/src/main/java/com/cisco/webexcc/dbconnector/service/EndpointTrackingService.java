package com.cisco.webexcc.dbconnector.service;

import com.cisco.webexcc.dbconnector.model.EndpointExecution;
import com.cisco.webexcc.dbconnector.model.EndpointStat;
import com.cisco.webexcc.dbconnector.model.EnvironmentStat;
import com.cisco.webexcc.dbconnector.repository.EndpointExecutionRepository;
import com.cisco.webexcc.dbconnector.repository.LdapStatementRepository;
import com.cisco.webexcc.dbconnector.repository.SqlStatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EndpointTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(EndpointTrackingService.class);

    @Autowired
    private EndpointExecutionRepository repository;

    @Autowired
    private SqlStatementRepository sqlStatementRepository;

    @Autowired
    private LdapStatementRepository ldapStatementRepository;

    @Transactional
    public void resetEnvironment(String env) {
        String envLower = env.toLowerCase();
        repository.deleteByEndpointStartingWith("/api/query/" + envLower + "/");
        repository.deleteByEndpointStartingWith("/api/ldap/query/" + envLower + "/");
    }

    @Transactional
    public void cleanupEnvironment(String env) {
        String normalizedEnv = env.toUpperCase();

        var configuredSqlNames = sqlStatementRepository.findByEnvironment(normalizedEnv).stream()
                .map(s -> s.getName().toLowerCase())
                .toList();
        cleanupTrackedEndpoints("/api/query/" + normalizedEnv.toLowerCase() + "/", configuredSqlNames);

        var configuredLdapNames = ldapStatementRepository.findByEnvironment(normalizedEnv).stream()
                .map(s -> s.getName().toLowerCase())
                .toList();
        cleanupTrackedEndpoints("/api/ldap/query/" + normalizedEnv.toLowerCase() + "/", configuredLdapNames);
    }

    public void trackExecution(String endpoint, int statusCode) {
        try {
            EndpointExecution execution = new EndpointExecution(endpoint.toLowerCase(), LocalDateTime.now(), statusCode);
            repository.save(execution);
        } catch (RuntimeException exception) {
            logger.warn("Skipping endpoint tracking for {} due to repository error: {}", endpoint, exception.getMessage());
        }
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

        stats.put("PROD", getCombinedEnvironmentStat("prod"));
        stats.put("UAT", getCombinedEnvironmentStat("uat"));
        stats.put("DEV", getCombinedEnvironmentStat("dev"));
        
        return stats;
    }

    public Map<String, Map<String, EndpointStat>> getDetailedEnvironmentStats() {
        Map<String, Map<String, EndpointStat>> stats = new LinkedHashMap<>();
        stats.put("PROD", getCombinedEndpointCounts("PROD"));
        stats.put("UAT", getCombinedEndpointCounts("UAT"));
        stats.put("DEV", getCombinedEndpointCounts("DEV"));
        return stats;
    }

    private EnvironmentStat getCombinedEnvironmentStat(String envLower) {
        long totalHits = safeCountByEndpointPrefix("/api/query/" + envLower + "/")
                + safeCountByEndpointPrefix("/api/ldap/query/" + envLower + "/");
        long failedHits = safeFailureCountByPrefix("/api/query/" + envLower + "/")
                + safeFailureCountByPrefix("/api/ldap/query/" + envLower + "/");
        return new EnvironmentStat(totalHits, failedHits);
    }

    private Map<String, EndpointStat> getCombinedEndpointCounts(String env) {
        Map<String, EndpointStat> merged = new HashMap<>();
        merged.putAll(getSqlEndpointCounts(env));
        merged.putAll(getLdapEndpointCounts(env));
        return merged;
    }

    private Map<String, EndpointStat> getSqlEndpointCounts(String env) {
        String prefix = "/api/query/" + env.toLowerCase() + "/";
        var configuredNames = sqlStatementRepository.findByEnvironment(env).stream()
                .map(s -> s.getName().toLowerCase())
                .toList();
        return getEndpointCounts(prefix, configuredNames);
    }

    private Map<String, EndpointStat> getLdapEndpointCounts(String env) {
        String prefix = "/api/ldap/query/" + env.toLowerCase() + "/";
        var configuredNames = ldapStatementRepository.findByEnvironment(env).stream()
                .map(s -> s.getName().toLowerCase())
                .toList();
        return getEndpointCounts(prefix, configuredNames);
    }

    private Map<String, EndpointStat> getEndpointCounts(String prefix, java.util.List<String> configuredNames) {
        Map<String, EndpointStat> map = new HashMap<>();

        for (String endpointName : configuredNames) {
            String key = prefix + endpointName;
            map.put(key, new EndpointStat(0L, 0L, null));
        }

        // 2. Overlay with actual execution stats
        java.util.List<Object[]> results = safeCountEndpointsByPrefix(prefix);
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
        java.util.List<Object[]> failureResults = safeCountEndpointFailuresByPrefix(prefix);
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

    private void cleanupTrackedEndpoints(String prefix, java.util.List<String> configuredNames) {
        java.util.List<Object[]> tracked = safeCountEndpointsByPrefix(prefix);
        for (Object[] entry : tracked) {
            String endpoint = (String) entry[0];
            String[] parts = endpoint.split("/");
            if (parts.length < 2) {
                repository.deleteByEndpoint(endpoint);
                continue;
            }

            String endpointName = parts[parts.length - 1].toLowerCase();
            if (!configuredNames.contains(endpointName)) {
                repository.deleteByEndpoint(endpoint);
            }
        }
    }

    private long safeCountByEndpointPrefix(String prefix) {
        try {
            return repository.countByEndpointStartingWith(prefix);
        } catch (RuntimeException exception) {
            logger.warn("Unable to read endpoint count for prefix {}: {}", prefix, exception.getMessage());
            return 0L;
        }
    }

    private long safeFailureCountByPrefix(String prefix) {
        try {
            return repository.countFailuresByPrefix(prefix);
        } catch (RuntimeException exception) {
            logger.warn("Unable to read endpoint failures for prefix {}: {}", prefix, exception.getMessage());
            return 0L;
        }
    }

    private java.util.List<Object[]> safeCountEndpointsByPrefix(String prefix) {
        try {
            return repository.countEndpointsByPrefix(prefix);
        } catch (RuntimeException exception) {
            logger.warn("Unable to read endpoint details for prefix {}: {}", prefix, exception.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    private java.util.List<Object[]> safeCountEndpointFailuresByPrefix(String prefix) {
        try {
            return repository.countEndpointFailuresByPrefix(prefix);
        } catch (RuntimeException exception) {
            logger.warn("Unable to read endpoint failure details for prefix {}: {}", prefix, exception.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}
