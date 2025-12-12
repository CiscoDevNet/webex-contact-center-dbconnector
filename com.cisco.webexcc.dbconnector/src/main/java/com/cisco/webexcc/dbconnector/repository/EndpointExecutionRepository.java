package com.cisco.webexcc.dbconnector.repository;

import com.cisco.webexcc.dbconnector.model.EndpointExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointExecutionRepository extends JpaRepository<EndpointExecution, Long> {
    long countByExecutionTimeAfter(LocalDateTime timestamp);
    long countByEndpointStartingWith(String prefix);

    @Query("SELECT e.endpoint, COUNT(e), MAX(e.executionTime) FROM EndpointExecution e WHERE e.endpoint LIKE ?1% GROUP BY e.endpoint")
    List<Object[]> countEndpointsByPrefix(String prefix);

    @Query("SELECT e.endpoint, COUNT(e) FROM EndpointExecution e WHERE e.endpoint LIKE ?1% AND (e.statusCode < 200 OR e.statusCode >= 300) GROUP BY e.endpoint")
    List<Object[]> countEndpointFailuresByPrefix(String prefix);

    @Query("SELECT COUNT(e) FROM EndpointExecution e WHERE e.statusCode < 200 OR e.statusCode >= 300")
    long countFailures();

    @Query("SELECT COUNT(e) FROM EndpointExecution e WHERE e.endpoint LIKE ?1% AND (e.statusCode < 200 OR e.statusCode >= 300)")
    long countFailuresByPrefix(String prefix);

    void deleteByEndpointStartingWith(String prefix);
    void deleteByEndpoint(String endpoint);
}
