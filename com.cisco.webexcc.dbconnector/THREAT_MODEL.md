# Threat Model - Webex Contact Center DB Connector

**Document Version:** 1.2  
**Date:** March 23, 2026  
**Application:** Webex Contact Center DB Connector  
**Framework:** STRIDE Threat Modeling

---

## Executive Summary

The Webex Contact Center DB Connector is a Spring Boot application that exposes database queries as REST API endpoints for Webex Contact Center integration. The application manages database connections, SQL statements, and provides OAuth2-based authentication for administrative access. This threat model identifies security risks using the STRIDE methodology and provides mitigation recommendations.

---

## Table of Contents

1. [System Overview](#1-system-overview)
2. [Architecture Components](#2-architecture-components)
3. [Data Flow Diagrams](#3-data-flow-diagrams)
4. [Trust Boundaries](#4-trust-boundaries)
5. [Assets](#5-assets)
6. [Threat Analysis (STRIDE)](#6-threat-analysis-stride)
7. [Risk Summary](#7-risk-summary)
8. [Mitigation Roadmap](#8-mitigation-roadmap)

---

## Recent Security Updates (February 2026)

### Recent Functional Surface Updates (March 2026)

### ✅ Expanded API Surface: LDAP Endpoint Family
- Added LDAP execution endpoint family: `/api/ldap/query/{env}/{name}` in addition to existing SQL route family.
- Added LDAP admin management and deploy workflow (DEV → UAT/PROD).
- Added unified test console execution for both SQL and LDAP.

### Security Implications
- Existing controls and recommendations for unauthenticated API execution routes now apply to both:
  - `/api/query/{env}/{name}`
  - `/api/ldap/query/{env}/{name}`
- Monitoring, rate-limiting, and ingress filtering should be configured consistently for both route families.
- Dashboard activity metrics now aggregate SQL + LDAP traffic, improving detection visibility across data-access endpoints.

### ✅ CVE Vulnerability Remediation
All critical and high-severity CVE vulnerabilities have been addressed through dependency upgrades:

**CRITICAL Severity Fixed:**
- ✅ **CVE-2025-24813** - Apache Tomcat RCE/information disclosure (upgraded to 10.1.50)

**HIGH Severity Fixed:**
- ✅ **CVE-2025-59250** - Microsoft SQL Server JDBC improper input validation (upgraded to 13.2.1.jre11)
- ✅ **CVE-2025-41249** - Spring Framework improper authorization (upgraded to 6.2.15)
- ✅ **CVE-2024-50379** - Apache Tomcat TOCTOU race condition (upgraded to 10.1.50)
- ✅ **CVE-2024-56337** - Apache Tomcat TOCTOU race condition (upgraded to 10.1.50)
- ✅ **CVE-2025-48988** - Apache Tomcat DoS in multipart upload (upgraded to 10.1.50)
- ✅ **CVE-2025-48989** - Apache Tomcat improper resource shutdown (upgraded to 10.1.50)
- ✅ **CVE-2025-55752** - Apache Tomcat path traversal (upgraded to 10.1.50)

**MEDIUM Severity Fixed:**
- ✅ **CVE-2025-41234** - Spring Framework reflected file download (upgraded to 6.2.15)
- ✅ **CVE-2025-41242** - Spring Framework path traversal (upgraded to 6.2.15)
- ✅ **CVE-2025-53864** - Nimbus JOSE JWT DoS vulnerability (upgraded to 10.0.2)

### ✅ Database Schema Protection Enhancements
- Configured `spring.jpa.hibernate.ddl-auto=validate` to prevent schema modifications
- Added `spring.jpa.properties.hibernate.hbm2ddl.auto=validate` for additional protection
- Set `spring.jpa.properties.javax.persistence.schema-generation.database.action=none`
- **Result**: No DROP, TRUNCATE, ALTER, or CREATE operations can be performed on the database schema

### Remaining Security Posture
With these updates, the application has **zero known CVE vulnerabilities** in its dependencies. The focus now shifts to application-level security controls detailed in this threat model.

---

## 1. System Overview

### Purpose
The DB Connector bridges Webex Contact Center with backend databases, allowing real-time data retrieval via dynamically generated API endpoints.

### Key Functionality
- **Database Connection Management**: Store and manage credentials for MySQL, SQL Server, and Oracle databases
- **SQL Statement Management**: Define parameterized SQL queries exposed as REST endpoints
- **API Execution**: Execute SQL queries via `/api/query/{env}/{name}` endpoints
- **OAuth2 Authentication**: Webex OAuth2 for administrative access
- **Multi-Environment Support**: DEV, UAT, PROD environment segregation

### Technology Stack
- **Language**: Java 24
- **Framework**: Spring Boot 3.5.9
- **Spring Framework**: 6.2.15
- **Security**: Spring Security 6.4.4 with OAuth2
- **Application Server**: Apache Tomcat 10.1.50 (embedded)
- **Database**: H2 2.3.232 (internal), MySQL/SQL Server/Oracle (external)
- **Authentication**: Webex OAuth2
- **JDBC Drivers**: 
  - SQL Server: mssql-jdbc 13.2.1.jre11
  - MySQL: mysql-connector-j 9.1.0
  - Oracle: ojdbc11 23.5.0.24.07

---

## 2. Architecture Components

### 2.1 External Components
- **Webex Contact Center**: External system invoking API endpoints
- **Webex Identity Broker**: OAuth2 provider for admin authentication
- **External Databases**: MySQL, SQL Server, Oracle databases containing business data
- **Load Balancer/Firewall**: Network security layer (customer-managed)

### 2.2 Application Components
- **ApiController**: Unauthenticated endpoint `/api/query/{env}/{name}` for query execution
- **AdminController**: Authenticated endpoints for managing connections and SQL statements
- **SqlExecutionService**: Dynamic JDBC connection and query execution
- **SecurityConfig**: Spring Security configuration with OAuth2
- **H2 Database**: Internal storage for connections, SQL statements, and execution stats
- **Repository Layer**: JPA repositories for data persistence

---

## 3. Data Flow Diagrams

### 3.1 API Query Execution Flow
```
[Webex CC] --HTTPS--> [Load Balancer] --HTTPS--> [/api/query/{env}/{name}] 
    --> [ApiController] --> [SqlExecutionService] --> [External DB]
    --> [Return JSON] --> [Webex CC]
```

### 3.2 Administrative Management Flow
```
[Admin User] --HTTPS--> [/admin/*] --> [OAuth2 Filter] --> [Webex IDP]
    --> [AdminController] --> [H2 Database] --> [Store/Retrieve Config]
```

### 3.3 Database Connection Flow
```
[SqlExecutionService] --> [DriverManagerDataSource] 
    --> [MySQL/SQLServer/Oracle Driver] --> [External Database]
```

---

## 4. Trust Boundaries

### Boundary 1: External Network → Application
- **Entry Points**: All HTTP/HTTPS endpoints
- **Controls**: Load balancer, IP whitelisting (customer-managed), TLS

### Compensating Controls and Risk Acceptance
- **Decision ID**: RA-2026-03-23-API-AUTH
- **Accepted Scope**: Unauthenticated access to execution routes (`/api/query/**`, `/api/ldap/**`) is accepted for current deployment.
- **Justification**: Service is deployed behind customer-managed firewall controls with source IP allowlisting.
- **Required Controls**:
  - Inbound access restricted to approved source IP ranges only.
  - No direct public internet exposure of application nodes.
  - TLS termination/encryption remains enforced at the edge.
  - Firewall and allowlist changes are change-controlled and auditable.
- **Review Triggers**:
  - Any internet exposure (directly or via proxy/load balancer).
  - New consumer integrations outside currently allowlisted ranges.
  - Security incident involving API endpoint abuse.
  - Annual security review cycle.

### Boundary 2: Application → External Databases
- **Entry Points**: JDBC connections to MySQL, SQL Server, Oracle
- **Controls**: Database credentials, network firewall rules

### Boundary 3: Unauthenticated API → Authenticated Admin
- **Entry Points**: `/api/*` (public) vs `/admin/*` (authenticated)
- **Controls**: Spring Security with permitAll vs authenticated

### Boundary 4: Application → Internal H2 Database
- **Entry Points**: JPA repositories
- **Controls**: File-based H2 with default credentials

---

## 5. Assets

### High-Value Assets
| Asset | Description | Confidentiality | Integrity | Availability |
|-------|-------------|-----------------|-----------|--------------|
| Database Credentials | Passwords for MySQL/Oracle/SQLServer | **CRITICAL** | HIGH | HIGH |
| SQL Statements | Business logic, schema information | HIGH | HIGH | MEDIUM |
| H2 Database | Contains all configuration data | **CRITICAL** | **CRITICAL** | HIGH |
| API Endpoints | Query execution interfaces | MEDIUM | **CRITICAL** | **CRITICAL** |
| Execution Logs | May contain sensitive data | HIGH | MEDIUM | LOW |
| OAuth2 Tokens | Admin session credentials | **CRITICAL** | HIGH | MEDIUM |

---

## 6. Threat Analysis (STRIDE)

### 6.1 Spoofing Threats

#### T1.1: Webex CC IP Spoofing
- **Threat**: Attacker spoofs Webex CC IP addresses to access API endpoints
- **Attack Vector**: `/api/query/{env}/{name}` has permitAll() access
- **Impact**: HIGH - Unauthorized data access from databases
- **Likelihood**: MEDIUM
- **Risk Decision**: **Accepted with compensating controls** for current deployment boundary (firewall + IP allowlist).
- **Current Controls**: 
  - Documentation recommends IP whitelisting (customer-managed)
  - No application-level authentication on API endpoints
- **Gaps**: No built-in IP validation or API key authentication
- **Mitigation**:
  - ✅ **Conditional PRIORITY 1**: Implement API key/token authentication for `/api/*` endpoints if deployment boundary changes (internet exposure, non-allowlisted consumers, or control drift)
  - ✅ Configure application-level IP whitelisting in SecurityConfig
  - ✅ Add request signature validation (HMAC-based)
  - ✅ Implement rate limiting per IP/API key

#### T1.2: OAuth2 Token Theft
- **Threat**: Stolen admin OAuth2 tokens used to access admin panel
- **Attack Vector**: Session hijacking, XSS, MITM
- **Impact**: CRITICAL - Full administrative access
- **Likelihood**: LOW-MEDIUM
- **Current Controls**: 
  - OAuth2 with Webex
  - Session cookies
- **Gaps**: 
  - CSRF disabled (`csrf.disable()`)
  - Session timeout warning is client-side only (can be bypassed if JavaScript is disabled)
  - Logout redirects to HTTP (not HTTPS) localhost
- **Mitigation**:
  - ✅ **PRIORITY 1**: Enable CSRF protection for admin endpoints
  - ✅ Configure short session timeouts (30 minutes)
  - ✅ Implement secure, same-site cookie flags
  - ✅ Fix logout URL to use HTTPS in production
  - ✅ Add IP address binding to sessions

### 6.2 Tampering Threats

#### T2.1: SQL Injection via Query Parameters
- **Threat**: SQL injection through URL query parameters on `/api/query/{env}/{name}`
- **Attack Vector**: Malicious input in query parameters (e.g., `?userId=1' OR '1'='1`)
- **Impact**: CRITICAL - Data breach, data manipulation, privilege escalation
- **Likelihood**: MEDIUM-HIGH
- **Current Controls**: 
  - Uses NamedParameterJdbcTemplate and JdbcTemplate with positional parameters
  - Parameters bound via prepared statements
- **Gaps**: 
  - No input validation on parameter values
  - No parameterization enforcement check
  - MaxRows=100 limit may not prevent resource exhaustion
- **Mitigation**:
  - ✅ **PRIORITY 1**: Add input validation middleware for all parameters
  - ✅ Implement allowlist validation for parameter names
  - ✅ Add SQL syntax analysis to block dangerous patterns in stored SQL
  - ✅ Enforce prepared statement usage verification
  - ✅ Log all suspicious parameter patterns

#### T2.2: Stored SQL Injection
- **Threat**: Admin stores malicious SQL statements that execute on API calls
- **Attack Vector**: Admin panel SQL statement creation
- **Impact**: CRITICAL - Arbitrary SQL execution
- **Likelihood**: LOW (requires admin access)
- **Current Controls**: 
  - Requires OAuth2 authentication
  - Uses parameterized queries for execution
- **Gaps**: 
  - No SQL statement validation before saving
  - Admins can store any SQL including DROP, DELETE, UPDATE
  - No read-only enforcement
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement SQL statement linting/validation
  - ✅ Restrict to SELECT statements only (block DML/DDL)
  - ✅ Add approval workflow for SQL statement creation
  - ✅ Use read-only database accounts where possible
  - ✅ Implement SQL statement change auditing

#### T2.3: Database Connection String Manipulation
- **Threat**: Malicious JDBC URL injection to attack internal networks
- **Attack Vector**: Admin panel connection creation with crafted URL
- **Impact**: HIGH - SSRF, internal network scanning, data exfiltration
- **Likelihood**: LOW (requires admin access)
- **Current Controls**: 
  - Requires OAuth2 authentication
  - URL stored in H2 database
- **Gaps**: 
  - No URL validation
  - No protocol restrictions
  - Can target internal services (file://, ldap://, etc.)
- **Mitigation**:
  - ✅ **PRIORITY 2**: Validate JDBC URL format and protocol
  - ✅ Allowlist permitted hostname patterns
  - ✅ Block access to private IP ranges (RFC 1918)
  - ✅ Implement connection timeout restrictions (5s timeout exists)

#### T2.4: H2 Database File Tampering
- **Threat**: Direct file system access to H2 database
- **Attack Vector**: File system access on server
- **Impact**: CRITICAL - Complete configuration compromise
- **Likelihood**: LOW (requires server access)
- **Current Controls**: 
  - File-based H2 at `./data/dbconnector`
  - H2 console disabled
  - ✅ **NEW**: Schema modifications prevented (ddl-auto=validate)
  - ✅ **NEW**: DROP/TRUNCATE operations blocked via JPA configuration
- **Gaps**: 
  - Default credentials (username: sa, password: password)
  - No file encryption
  - File permissions not specified
- **Mitigation**:
  - ✅ **PRIORITY 1**: Change default H2 credentials immediately
  - ✅ Enable H2 database encryption
  - ✅ Set restrictive file permissions (600)
  - ✅ Store H2 credentials in environment variables
  - ✅ Consider moving to external database (PostgreSQL)
- **Status Update (Feb 2026)**: Schema protection implemented, credential hardening still required

### 6.3 Repudiation Threats

#### T3.1: Insufficient API Audit Logging
- **Threat**: Cannot trace unauthorized API access or data exfiltration
- **Attack Vector**: API abuse without detection
- **Impact**: MEDIUM - Compliance violations, undetected breaches
- **Likelihood**: HIGH
- **Current Controls**: 
  - Execution tracking stored in database
  - Logging with SLF4J
- **Gaps**: 
  - No logging of query parameters (may contain sensitive data)
  - No IP address logging
  - No correlation IDs
  - Logs may not be immutable
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement comprehensive audit logging
  - ✅ Log: timestamp, IP, endpoint, parameters (sanitized), response status
  - ✅ Add correlation IDs to all requests
  - ✅ Implement centralized logging (Splunk, ELK)
  - ✅ Set up log integrity verification

#### T3.2: Admin Action Repudiation
- **Threat**: Admin denies creating/modifying malicious SQL or connections
- **Attack Vector**: No attribution for configuration changes
- **Impact**: MEDIUM - Cannot prove accountability
- **Likelihood**: MEDIUM
- **Current Controls**: 
  - OAuth2 authentication provides user identity
- **Gaps**: 
  - No audit trail for admin actions
  - No "created by" or "modified by" fields in entities
  - No change history
- **Mitigation**:
  - ✅ **PRIORITY 2**: Add audit fields (createdBy, createdAt, modifiedBy, modifiedAt)
  - ✅ Implement change history table
  - ✅ Log all admin operations with user identity
  - ✅ Implement approval workflow for critical changes

### 6.4 Information Disclosure Threats

#### T4.1: Database Credentials in Memory
- **Threat**: Database passwords exposed in heap dumps or error logs
- **Attack Vector**: Error messages, logging, debugging
- **Impact**: CRITICAL - Full database compromise
- **Likelihood**: MEDIUM
- **Current Controls**: 
  - Passwords stored in H2 database
  - Standard Java security
- **Gaps**: 
  - Passwords stored in plain text in H2
  - No password encryption at rest
  - DbConnection.toString() could leak password
  - Error messages may expose credentials
- **Mitigation**:
  - ✅ **PRIORITY 1**: Encrypt passwords in H2 using Jasypt or Spring Cloud Vault
  - ✅ Override toString() to exclude password field
  - ✅ Implement SecretString or char[] for password handling
  - ✅ Configure logback to sanitize sensitive data
  - ✅ Add @JsonIgnore to password field in REST responses

#### T4.2: SQL Statement Exposure
- **Threat**: SQL queries reveal database schema and business logic
- **Attack Vector**: Unauthorized access to admin panel or API endpoint enumeration
- **Impact**: MEDIUM - Schema disclosure aids targeted attacks
- **Likelihood**: MEDIUM
- **Current Controls**: 
  - Admin panel requires authentication
- **Gaps**: 
  - API endpoint naming may be predictable
  - Error messages may reveal SQL syntax
  - No obfuscation of SQL content
- **Mitigation**:
  - ✅ **PRIORITY 3**: Implement generic error messages
  - ✅ Use UUIDs instead of predictable names for endpoints
  - ✅ Add rate limiting to prevent endpoint enumeration
  - ✅ Sanitize error responses

#### T4.3: Verbose Error Messages
- **Threat**: Stack traces and detailed errors expose internals
- **Attack Vector**: Invalid requests, SQL errors
- **Impact**: MEDIUM - Aids reconnaissance
- **Likelihood**: HIGH
- **Current Controls**: 
  - Spring Boot default error handling
- **Gaps**: 
  - Stack traces visible in API responses (AdminController L122)
  - Database error messages passed to client
  - Detailed SQL errors may be exposed
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement custom error handler
  - ✅ Return generic error messages to external clients
  - ✅ Log detailed errors server-side only
  - ✅ Configure Spring Boot error.include-stacktrace=never

#### T4.4: H2 Console Exposure
- **Threat**: H2 console provides direct database access if enabled
- **Attack Vector**: Accessing `/h2-console` endpoint
- **Impact**: CRITICAL - Full configuration database access
- **Likelihood**: LOW (currently disabled)
- **Current Controls**: 
  - `spring.h2.console.enabled=false`
- **Gaps**: 
  - Could be accidentally enabled
  - No additional protection if enabled
- **Mitigation**:
  - ✅ **PRIORITY 3**: Add runtime check to prevent H2 console in production
  - ✅ Document security risk in configuration
  - ✅ Implement profile-based activation (dev only)

### 6.5 Denial of Service Threats

#### T5.1: Resource Exhaustion via Large Result Sets
- **Threat**: Queries returning massive datasets exhaust memory/CPU
- **Attack Vector**: Crafted parameters causing full table scans
- **Impact**: HIGH - Service unavailability
- **Likelihood**: MEDIUM
- **Current Controls**: 
  - `jdbcTemplate.setMaxRows(100)`
- **Gaps**: 
  - MaxRows may not prevent large data transfer before limiting
  - No query timeout beyond connection timeout
  - No monitoring of slow queries
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement query timeout (e.g., 30 seconds)
  - ✅ Add request throttling per IP/endpoint
  - ✅ Implement circuit breaker for database connections
  - ✅ Monitor and alert on slow queries
  - ✅ Consider pagination for large result sets

#### T5.2: Connection Pool Exhaustion
- **Threat**: Excessive API calls exhaust HikariCP connection pool
- **Attack Vector**: DDoS or malicious high-volume requests
- **Impact**: HIGH - Service unavailability
- **Likelihood**: MEDIUM
- **Current Controls**: 
  - HikariCP with max pool size 20
  - Connection timeout (implicit)
- **Gaps**: 
  - No rate limiting
  - No connection leak detection
  - No per-client connection limits
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement rate limiting (Spring RateLimiter)
  - ✅ Configure connection leak detection
  - ✅ Set aggressive connection timeout (30s)
  - ✅ Implement health check endpoint
  - ✅ Add connection pool metrics/alerting

#### T5.3: Slow SQL Statement DoS
- **Threat**: Admin creates intentionally slow SQL causing DoS
- **Attack Vector**: Complex queries with Cartesian products, no indexes
- **Impact**: HIGH - Service degradation
- **Likelihood**: LOW (requires admin access)
- **Current Controls**: 
  - 5-second test connection timeout
- **Gaps**: 
  - No query execution timeout for API calls
  - No query complexity analysis
  - No query plan review
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement statement timeout for all queries
  - ✅ Add query performance testing in test page
  - ✅ Implement query complexity analyzer
  - ✅ Alert on queries exceeding time threshold

#### T5.4: H2 Database Lock Contention
- **Threat**: High write volume to H2 causes lock contention
- **Attack Vector**: Concurrent admin operations
- **Impact**: MEDIUM - Admin panel slowness
- **Likelihood**: LOW
- **Current Controls**: 
  - H2 file-based database
- **Gaps**: 
  - H2 not optimized for high concurrency
  - Single file lock
- **Mitigation**:
  - ✅ **PRIORITY 3**: Migrate to external database (PostgreSQL)
  - ✅ Implement connection pooling for H2
  - ✅ Add read replicas if needed

### 6.6 Elevation of Privilege Threats

#### T6.1: Database Account Privilege Escalation
- **Threat**: Stored queries exploit database admin accounts
- **Attack Vector**: SQL statements using overprivileged database accounts
- **Impact**: CRITICAL - Database takeover
- **Likelihood**: LOW-MEDIUM
- **Current Controls**: 
  - Depends on customer database configuration
- **Gaps**: 
  - No enforcement of read-only accounts
  - No privilege separation
  - Single account per connection
- **Mitigation**:
  - ✅ **PRIORITY 1**: Document requirement for read-only database accounts
  - ✅ Implement database permission check on connection test
  - ✅ Add warning if connection has DML/DDL privileges
  - ✅ Consider separate accounts for admin testing vs API execution

#### T6.2: OAuth2 Scope Escalation
- **Threat**: OAuth token with insufficient scope gains admin access
- **Attack Vector**: Token manipulation, scope vulnerabilities
- **Impact**: HIGH - Unauthorized admin access
- **Likelihood**: LOW
- **Current Controls**: 
  - OAuth2 with Webex, scope: `spark:people_read`
- **Gaps**: 
  - No role-based access control (RBAC)
  - All authenticated users are admins
  - No fine-grained permissions
- **Mitigation**:
  - ✅ **PRIORITY 2**: Implement RBAC with roles (Viewer, Editor, Admin)
  - ✅ Validate OAuth scope on each request
  - ✅ Add group-based authorization via Webex
  - ✅ Implement method-level security annotations

#### T6.3: Path Traversal in File Operations
- **Threat**: If application handles file uploads/downloads, path traversal risk
- **Attack Vector**: `../../etc/passwd` style attacks
- **Impact**: HIGH - Arbitrary file access
- **Likelihood**: N/A (no file operations identified)
- **Current Controls**: None needed
- **Mitigation**: 
  - ✅ Document that file operations should use Path.normalize()

#### T6.4: Insecure Deserialization
- **Threat**: If application deserializes untrusted data
- **Attack Vector**: Malicious serialized objects
- **Impact**: CRITICAL - Remote code execution
- **Likelihood**: LOW (no obvious deserialization)
- **Current Controls**: 
  - Standard JSON parsing with Jackson
- **Gaps**: 
  - Could be introduced in future
- **Mitigation**:
  - ✅ **PRIORITY 3**: Configure Jackson to reject polymorphic types
  - ✅ Avoid Java serialization entirely
  - ✅ Implement input validation on all JSON endpoints

---

## 7. Risk Summary

### Critical Risks (Immediate Action Required)

| ID | Threat | Risk Level | Priority |
|----|--------|------------|----------|
| T1.1 | Unauthenticated API endpoints | CRITICAL | P1 |
| T1.2 | CSRF disabled, insecure session | CRITICAL | P1 |
| T2.1 | SQL injection via parameters | CRITICAL | P1 |
| T2.4 | Default H2 credentials | CRITICAL | P1 |
| T4.1 | Plaintext password storage | CRITICAL | P1 |
| T6.1 | Overprivileged database accounts | CRITICAL | P1 |

### High Risks (Plan Mitigation)

| ID | Threat | Risk Level | Priority |
|----|--------|------------|----------|
| T2.2 | Stored SQL injection | HIGH | P2 |
| T2.3 | JDBC URL manipulation | HIGH | P2 |
| T5.1 | Resource exhaustion | HIGH | P2 |
| T5.2 | Connection pool exhaustion | HIGH | P2 |

### Medium Risks (Monitor & Address)

| ID | Threat | Risk Level | Priority |
|----|--------|------------|----------|
| T3.1 | Insufficient audit logging | MEDIUM | P2 |
| T3.2 | Admin action repudiation | MEDIUM | P2 |
| T4.2 | SQL statement exposure | MEDIUM | P3 |
| T4.3 | Verbose error messages | MEDIUM | P2 |

---

## 8. Mitigation Roadmap

### Phase 0: Dependency Security (COMPLETED - Feb 2026)

#### 0.1 CVE Vulnerability Remediation ✅ COMPLETED
- [x] Audit all dependencies for known CVEs
- [x] Upgrade Spring Boot to 3.5.9 (from 3.4.0)
- [x] Upgrade Spring Framework to 6.2.15 (from 6.2.0)
- [x] Upgrade Apache Tomcat to 10.1.50 (from 10.1.33)
- [x] Upgrade mssql-jdbc to 13.2.1.jre11 (from 12.8.1.jre11)
- [x] Upgrade nimbus-jose-jwt to 10.0.2 (from 9.37.3)
- [x] Verify zero known CVEs in dependency tree

#### 0.2 Database Schema Protection ✅ COMPLETED
- [x] Configure hibernate.ddl-auto=validate
- [x] Disable schema generation operations
- [x] Block DROP, TRUNCATE, ALTER, CREATE operations
- [x] Test schema validation on startup

#### 0.3 Ongoing Vulnerability Management
- [ ] Establish monthly CVE scanning schedule
- [ ] Configure automated dependency vulnerability alerts
- [ ] Document upgrade testing procedures
- [ ] Create rollback plan for failed upgrades

### Phase 1: Critical Security Hardening (Weeks 1-2)

#### 1.1 API Endpoint Authentication
- [ ] Implement API key authentication for `/api/query/**` (required if RA-2026-03-23-API-AUTH conditions are not met)
- [ ] Add API key management in admin panel
- [ ] Configure request signing (HMAC-SHA256)
- [ ] Implement rate limiting (100 req/min per key)

#### 1.2 Credential Security
- [ ] Change default H2 credentials
- [ ] Implement password encryption (Jasypt/Spring Vault)
- [ ] Move H2 credentials to environment variables
- [ ] Enable H2 database file encryption

#### 1.3 Session Security
- [ ] Enable CSRF protection for admin endpoints
- [ ] Configure 30-minute session timeout
- [ ] Set secure, HTTP-only, same-site cookies
- [ ] Fix logout URL for production (HTTPS)

#### 1.4 Input Validation
- [ ] Add parameter validation middleware
- [ ] Implement allowlist for parameter names
- [ ] Validate SQL statements on creation (SELECT only)
- [ ] Sanitize error messages

### Phase 2: Access Control & Auditing (Weeks 3-4)

#### 2.1 RBAC Implementation
- [ ] Define roles: Viewer, Editor, Admin
- [ ] Implement role-based authorization
- [ ] Add group mapping from Webex OAuth
- [ ] Apply method-level security

#### 2.2 Audit Logging
- [ ] Add audit fields to entities (createdBy, modifiedBy, timestamps)
- [ ] Implement comprehensive API request logging
- [ ] Log all admin actions with user identity
- [ ] Configure centralized logging

#### 2.3 Database Security
- [ ] Document read-only account requirement
- [ ] Implement privilege check on connection test
- [ ] Add warnings for overprivileged accounts
- [ ] Validate JDBC URL format and protocol

### Phase 3: Resilience & Monitoring (Weeks 5-6)

#### 3.1 DoS Prevention
- [ ] Implement query timeout (30 seconds)
- [ ] Configure connection leak detection
- [ ] Add circuit breaker for database connections
- [ ] Implement request throttling

#### 3.2 Monitoring & Alerting
- [ ] Add health check endpoint
- [ ] Implement connection pool metrics
- [ ] Configure alerts for slow queries
- [ ] Set up performance monitoring

#### 3.3 Error Handling
- [ ] Implement custom error handler
- [ ] Configure generic error messages
- [ ] Set `error.include-stacktrace=never`
- [ ] Sanitize all external error responses

### Phase 4: Architecture Improvements (Weeks 7-8)

#### 4.1 Database Migration
- [ ] Evaluate migration from H2 to PostgreSQL
- [ ] Implement database migration strategy
- [ ] Configure backup and recovery
- [ ] Set up replication if needed

#### 4.2 Security Hardening
- [ ] Configure IP whitelisting at application level
- [ ] Implement correlation IDs
- [ ] Add SQL complexity analyzer
- [ ] Configure Jackson security settings

#### 4.3 Documentation
- [ ] Document security best practices
- [ ] Create incident response plan
- [ ] Document deployment security checklist
- [ ] Create security testing procedures

---

## Appendix A: Security Configuration Checklist

### Pre-Production Deployment Checklist

- [ ] **Authentication**
  - [ ] API key authentication enabled
  - [ ] OAuth2 configured with production URLs
  - [ ] CSRF protection enabled
  - [ ] Session timeout configured (30 min)

- [ ] **Credentials**
  - [ ] H2 default password changed
  - [ ] H2 credentials in environment variables
  - [ ] Database passwords encrypted at rest
  - [ ] Secrets not in source control

- [ ] **Network**
  - [ ] IP whitelisting configured
  - [ ] HTTPS enforced (TLS 1.2+)
  - [ ] Load balancer configured
  - [ ] Firewall rules applied

- [ ] **Database**
  - [ ] Read-only database accounts used
  - [ ] Connection strings validated
  - [ ] Connection timeouts configured
  - [ ] Pool size limits set

- [ ] **Monitoring**
  - [ ] Audit logging enabled
  - [ ] Centralized logging configured
  - [ ] Alerts configured
  - [ ] Health checks enabled

- [ ] **Application**
  - [ ] H2 console disabled
  - [ ] Stack traces disabled
  - [ ] Error messages sanitized
  - [ ] Rate limiting enabled

---

## Appendix B: Secure Coding Guidelines

### Input Validation
```java
// ✅ Good: Validate parameter names
private static final Pattern SAFE_PARAM_NAME = Pattern.compile("^[a-zA-Z0-9_]{1,50}$");
if (!SAFE_PARAM_NAME.matcher(paramName).matches()) {
    throw new IllegalArgumentException("Invalid parameter name");
}
```

### Password Handling
```java
// ✅ Good: Encrypt passwords
@Convert(converter = PasswordEncryptor.class)
private String password;

// ✅ Good: Exclude from JSON
@JsonIgnore
public String getPassword() { return password; }
```

### Error Handling
```java
// ❌ Bad: Exposes internals
catch (Exception e) {
    return ResponseEntity.status(500).body(e.getMessage());
}

// ✅ Good: Generic message
catch (Exception e) {
    logger.error("Query execution failed", e);
    return ResponseEntity.status(500).body("Internal server error");
}
```

### SQL Statement Validation
```java
// ✅ Good: Restrict to SELECT
if (!sql.trim().toUpperCase().startsWith("SELECT")) {
    throw new IllegalArgumentException("Only SELECT statements allowed");
}
```

---

## Appendix C: Incident Response

### Security Incident Types

1. **Unauthorized API Access**
   - Review access logs for suspicious IPs
   - Rotate API keys
   - Check for data exfiltration
   - Notify affected customers

2. **Credential Compromise**
   - Immediately rotate affected credentials
   - Audit all database access
   - Review H2 database for tampering
   - Reset admin OAuth sessions

3. **SQL Injection Attack**
   - Identify affected endpoints
   - Review audit logs for data access
   - Patch vulnerable code
   - Notify stakeholders

4. **Denial of Service**
   - Enable rate limiting
   - Block attacking IPs
   - Scale resources if needed
   - Review application logs

---

## Document Control

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2026-01-30 | Security Team | Initial threat model |
| 1.1 | 2026-02-19 | Security Team | Updated for CVE remediation, Spring Boot 3.5.9 upgrade, database schema protection implementation |

**Review Schedule**: Quarterly or after significant architecture changes

**Next Review Date**: April 30, 2026

**Recent Security Improvements**:
- All critical and high-severity CVEs addressed
- Database schema protection implemented
- Technology stack updated to latest secure versions

## v2.1.2 Documentation Update (2026-03-23)

- Added LDAP Help button coverage across LDAP admin pages for consistency:
  - `/admin/ldap` (statements list)
  - `/admin/ldap/add` and `/admin/ldap/edit/{id}` (add/edit form)
  - `/admin/ldap/deploy/{id}` (deploy form)
- Added new LDAP help content pages:
  - `/help_ldap.html`
  - `/help_ldap_edit.html`
