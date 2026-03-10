package com.cisco.webexcc.dbconnector.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class SchemaMigrationService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SchemaMigrationService.class);

    private final JdbcTemplate jdbcTemplate;

    public SchemaMigrationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String dbProduct = connection.getMetaData().getDatabaseProductName();
            if (dbProduct == null || !dbProduct.toLowerCase().contains("h2")) {
                return;
            }

            Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'DB_CONNECTIONS'",
                    Integer.class
            );

            if (tableCount == null || tableCount == 0) {
                return;
            }

            jdbcTemplate.execute("ALTER TABLE db_connections ALTER COLUMN type VARCHAR(32)");
            logger.info("Schema migration applied: db_connections.type altered to VARCHAR(32)");
        } catch (Exception ex) {
            logger.debug("Schema migration skipped or already applied: {}", ex.getMessage());
        }
    }
}