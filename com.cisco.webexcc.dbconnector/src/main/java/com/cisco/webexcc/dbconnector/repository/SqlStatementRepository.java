package com.cisco.webexcc.dbconnector.repository;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.SqlStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SqlStatementRepository extends JpaRepository<SqlStatement, UUID> {
    Optional<SqlStatement> findByNameAndEnvironment(String name, String environment);
    Optional<SqlStatement> findByNameIgnoreCaseAndEnvironment(String name, String environment);
    List<SqlStatement> findByEnvironment(String environment);
    List<SqlStatement> findByDbConnection(DbConnection dbConnection);
    long countByDbConnection(DbConnection dbConnection);
}
