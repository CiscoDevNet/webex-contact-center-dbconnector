package com.cisco.webexcc.dbconnector.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "ldap_statements", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "environment"})
})
public class LdapStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String baseDn;

    @Column(length = 5000)
    private String filterContent;

    @Column(length = 2000)
    private String attributes;

    @Column(length = 1000)
    private String paramNames;

    private String environment = "DEV";

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private DbConnection dbConnection;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(String filterContent) {
        this.filterContent = filterContent;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getParamNames() {
        return paramNames;
    }

    public void setParamNames(String paramNames) {
        this.paramNames = paramNames;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}