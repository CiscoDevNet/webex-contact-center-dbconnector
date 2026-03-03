# Webex Contact Center DB Connector - User Guide

Welcome to the Webex Contact Center DB Connector. This application provides a secure and manageable way to expose database queries as API endpoints for use within Webex Contact Center flows.

## v2.1.1 Documentation Update (2026-03-03)

- Added LDAP statement management and execution.
- Test Console supports both SQL and LDAP endpoint testing.
- Added LDAP deploy flow from DEV to UAT/PROD.
- Home dashboard stats include combined SQL + LDAP activity.

## Table of Contents
1.  [Getting Started](#1-getting-started)
2.  [Dashboard](#2-dashboard)
3.  [Managing Connections](#3-managing-connections)
4.  [Managing SQL Statements](#4-managing-sql-statements)
5.  [Managing LDAP Statements](#5-managing-ldap-statements)
6.  [Testing Endpoints](#6-testing-endpoints)
7.  [Deployment](#7-deployment)

---

## 1. Getting Started

### Login Page
*   **Access**: Navigate to the root URL (e.g., `http://localhost:8080/`).
*   **Action**: Click the **"Login with Webex"** button.
*   **Process**: You will be redirected to the Webex sign-in page. Enter your credentials. Upon successful authentication, you will be redirected to the Dashboard.

---

## 2. Dashboard

### Home Page
*   **Access**: Automatically redirected here after login, or click **"Dashboard"** in the navigation menu.
*   **Overview**: Provides a high-level view of the system's health and usage.
*   **Key Features**:
    *   **Environment Stats**: Cards displaying the total number of API hits and failures for **PROD**, **UAT**, and **DEV** environments.
    *   **Detailed Activity**: Expandable lists for each environment showing specific endpoint usage.
        *   **Endpoint Name**: The name of the query being accessed.
        *   **Count**: How many times it has been executed.
        *   **Last Accessed**: Timestamp of the last execution.
    *   **User Profile**: Displays your Webex display name and email.

---

## 3. Managing Connections

### Connections Page
*   **Access**: Click **"Connections"** in the navigation menu.
*   **Purpose**: Manage the database credentials and connection details.
*   **Actions**:
    *   **View List**: See all configured database connections.
    *   **Add Connection**: Click the **"Add Connection"** button to register a new database.
        *   **Fields**: Name, Host, Port, Database Name, Username, Password, Type (MySQL, Postgres, etc.).
    *   **Test Connection**: Click the **"Test"** button next to a connection to verify the application can reach the database.
    *   **Edit/Delete**: Update credentials or remove obsolete connections.

---

## 4. Managing SQL Statements

### SQL Statements Page
*   **Access**: Click **"SQL Statements"** in the navigation menu.
*   **Purpose**: Define the SQL queries that will be exposed as APIs.
*   **Actions**:
    *   **View List**: See all defined queries, their associated environment, and connection.
    *   **Create Query**: Click **"Add SQL Statement"**.
        *   **Name**: A unique identifier (e.g., `getCustomerById`). This will be part of the API URL.
        *   **Environment**: Select DEV, UAT, or PROD.
        *   **Connection**: Choose which database this query runs against.
        *   **SQL**: Enter the SQL query. Use named parameters with colons (e.g., `SELECT * FROM users WHERE id = :userId`).
    *   **Edit**: Modify the SQL or connection details.

---

## 5. Managing LDAP Statements

### LDAP Statements Page
*   **Access**: Click **"LDAP Statements"** in the navigation menu.
*   **Purpose**: Define LDAP queries that are exposed as APIs.
*   **Actions**:
    *   **View List**: See LDAP statements by environment/connection.
    *   **Add LDAP Statement**: Define name, LDAP connection, base DN, filter, attributes, and parameters.
    *   **Edit/Delete**: Maintain lifecycle of LDAP endpoints.
    *   **Deploy (DEV only)**: Promote to UAT/PROD LDAP connections.

---

## 6. Testing Endpoints

### Test Page
*   **Access**: Click **"Test"** in the navigation menu.
*   **Purpose**: Validate SQL and LDAP endpoint behavior before flow integration.
*   **How to Use**:
    1.  **Select Environment**: Choose the environment (e.g., DEV).
    2.  **Select Endpoint**: Choose SQL or LDAP endpoint from the list.
    3.  **Enter Parameters**: Provide the required values for the selected endpoint.
    4.  **Run**: Click **"Execute"**.
    5.  **Results**: Review JSON response and status panel.

---

## 7. Deployment

### Deployment Page
*   **Access**: Via **Deploy** on SQL or LDAP statements pages.
*   **Purpose**: Promote tested DEV endpoints to UAT/PROD.
*   **Process**:
    1.  Select source statement (SQL or LDAP).
    2.  Choose target UAT/PROD connection of matching type.
    3.  Confirm overwrite if same-name endpoint already exists.

---

## 7. About
*   **Access**: Click **"About"** in the navigation menu.
*   **Content**: Displays application version, build information, and support contacts.
