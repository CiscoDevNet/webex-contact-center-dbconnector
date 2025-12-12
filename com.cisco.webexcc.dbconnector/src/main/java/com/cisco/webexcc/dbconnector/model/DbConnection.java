package com.cisco.webexcc.dbconnector.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "db_connections")
public class DbConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    
    @Enumerated(EnumType.STRING)
    private DbType type;
    
    private String url;
    private String username;
    private String password;
    
    private String environment = "DEV"; // DEV, UAT or PROD

    @Transient
    private long endpointCount;

    public enum DbType {
        MYSQL, SQLSERVER, ORACLE
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DbType getType() { return type; }
    public void setType(DbType type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public long getEndpointCount() { return endpointCount; }
    public void setEndpointCount(long endpointCount) { this.endpointCount = endpointCount; }
}
