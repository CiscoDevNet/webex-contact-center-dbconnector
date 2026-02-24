# Database Support Matrix

This document lists the databases currently supported by **Webex Contact Center DB Connector** based on the active codebase.

## Supported Databases

### 1) MySQL
- **Connection type value**: `MYSQL`
- **JDBC driver class**: `com.mysql.cj.jdbc.Driver`
- **Maven dependency**: `com.mysql:mysql-connector-j`

### 2) Microsoft SQL Server
- **Connection type value**: `SQLSERVER`
- **JDBC driver class**: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
- **Maven dependency**: `com.microsoft.sqlserver:mssql-jdbc`

### 3) Oracle Database
- **Connection type value**: `ORACLE`
- **JDBC driver class**: `oracle.jdbc.OracleDriver`
- **Maven dependency**: `com.oracle.database.jdbc:ojdbc11`

## Internal Application Database

The application uses **H2** as its internal persistence store (for connector metadata, SQL statements, etc.):
- **JDBC URL (default)**: `jdbc:h2:file:./data/dbconnector`
- **Driver class**: `org.h2.Driver`
- **Maven dependency**: `com.h2database:h2`

> H2 is used internally by the app and is not one of the selectable external connection types in the UI/API.

## Not Currently Supported (in active runtime selection)

The active `DbType` enum supports only:
- `MYSQL`
- `SQLSERVER`
- `ORACLE`

That means databases such as **PostgreSQL** are **not currently supported** unless code and dependencies are extended.

## Extensibility

This app is **not limited** to only the currently listed database types.

- The current list reflects the built-in `DbType` values enabled out of the box.
- The architecture is JDBC-based, so adding support for other modern standard databases is typically a small change (driver dependency + new `DbType` + driver mapping).
- This project is open source, so you are encouraged to adapt and extend it to fit your environment and requirements.

## Source of Truth in Code

- `com.cisco.webexcc.dbconnector/src/main/java/com/cisco/webexcc/dbconnector/model/DbConnection.java`
- `com.cisco.webexcc.dbconnector/src/main/java/com/cisco/webexcc/dbconnector/service/SqlExecutionService.java`
- `com.cisco.webexcc.dbconnector/pom.xml`
