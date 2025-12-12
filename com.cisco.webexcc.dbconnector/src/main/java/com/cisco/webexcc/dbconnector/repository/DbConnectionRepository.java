package com.cisco.webexcc.dbconnector.repository;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DbConnectionRepository extends JpaRepository<DbConnection, UUID> {
    List<DbConnection> findByEnvironment(String environment);
    List<DbConnection> findByEnvironmentNot(String environment);
}
