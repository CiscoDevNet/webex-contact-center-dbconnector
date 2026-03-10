package com.cisco.webexcc.dbconnector.repository;

import com.cisco.webexcc.dbconnector.model.DbConnection;
import com.cisco.webexcc.dbconnector.model.LdapStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LdapStatementRepository extends JpaRepository<LdapStatement, UUID> {
    Optional<LdapStatement> findByNameIgnoreCaseAndEnvironment(String name, String environment);
    List<LdapStatement> findByEnvironment(String environment);
    List<LdapStatement> findByDbConnection(DbConnection dbConnection);
    long countByDbConnection(DbConnection dbConnection);
}