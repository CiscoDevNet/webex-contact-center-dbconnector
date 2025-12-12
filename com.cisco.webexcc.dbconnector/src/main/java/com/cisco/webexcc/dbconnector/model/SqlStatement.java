package com.cisco.webexcc.dbconnector.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "sql_statements", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "environment"})
})
public class SqlStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name; // This will be the endpoint path identifier

    @Column(length = 1000)
    private String description;

    @Column(length = 5000)
    private String sqlContent;

    private String environment = "DEV"; // DEV or PROD

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private DbConnection dbConnection;

    @Column(length = 1000)
    private String paramNames; // Comma-separated list of parameter names for positional and named parameters

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSqlContent() { return sqlContent; }
    public void setSqlContent(String sqlContent) { this.sqlContent = sqlContent; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public DbConnection getDbConnection() { return dbConnection; }
    public void setDbConnection(DbConnection dbConnection) { this.dbConnection = dbConnection; }
    public String getParamNames() { return paramNames; }
    public void setParamNames(String paramNames) { this.paramNames = paramNames; }
}
