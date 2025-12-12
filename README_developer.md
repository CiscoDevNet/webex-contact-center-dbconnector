# Developer Documentation

This document outlines the development progress and configuration for the Webex Contact Center DB Connector project.

## Project Overview

This is a Spring Boot application designed to integrate with Webex Contact Center using OAuth 2.0 authentication.

## Technical Stack

*   **Framework**: Spring Boot 3.4.0
*   **Language**: Java 24
*   **Build Tool**: Maven
*   **Template Engine**: Thymeleaf
*   **Security**: Spring Security OAuth2 Client

## Features Implemented

### 1. Project Setup & Configuration
*   Scaffolded a standard Spring Boot application.
*   Updated `pom.xml` to include necessary dependencies:
    *   `spring-boot-starter-web`
    *   `spring-boot-starter-security`
    *   `spring-boot-starter-oauth2-client`
    *   `spring-boot-starter-thymeleaf`

### 2. Webex OAuth 2.0 Authentication
*   Configured `application.properties` with Webex OAuth2 provider details.
*   **Scopes**:
    *   `spark:people_read` (Required to fetch user profile/name)
*   **Redirect URI**: Configured as `{baseUrl}/login/oauth2/code/{registrationId}`.
    *   *Note*: The Webex Integration in the developer portal must have `http://localhost:8080/login/oauth2/code/webex` added as a valid Redirect URI.

### 3. Security Configuration (`SecurityConfig.java`)
*   Implemented a `SecurityFilterChain`.
*   Public access allowed for: `/`, `/login**`, `/error**`.
*   All other requests require authentication.
*   **Login Flow**:
    *   Initiated via `/oauth2/authorization/webex`.
    *   On success, redirects to `/welcome`.
*   **Logout**: Clears session and redirects to root `/`.

### 4. Web Controller (`WebController.java`)
*   **`/` (Root)**: Checks authentication status.
    *   If authenticated: Redirects to `/welcome`.
    *   If anonymous: Renders `index.html` (Login page).
*   **`/welcome`**: Displays a welcome message to the authenticated user.
*   **`/home`**: Displays detailed user attributes (Dashboard).

### 5. UI Templates
*   **`index.html`**: Simple landing page with a "Login with Webex" button.
*   **`welcome.html`**: Post-login landing page.
*   **`home.html`**: Debug view showing user details and attributes.

### 6. Utilities
*   **`backup.sh`**: A shell script to:
    1.  Run `mvn clean`.
    2.  Zip the project contents (excluding `.git`) to a timestamped file in `../webex-contact-center-dbconnectorV2_zip`.
    3.  Run `mvn clean package -DskipTests` to verify the build after backup.
*   **Startup Logging**: `DbConnectorApplication.java` implements `CommandLineRunner` to print the calculated Redirect URI and Base URL to the console on startup for easier debugging.

### 7. Observability & Logging
*   **AOP Logging**: Implemented `LoggingAspect` to log execution time for all methods in `ApiController`.
*   **Logback Configuration**: Customized `logback-spring.xml` to output logs to `logs/app.log` with a specific pattern and rolling policy.
*   **Logger Abbreviation**: Configured log pattern to abbreviate package names (e.g., `c.c.w.d.aspect.LoggingAspect`) for cleaner output.

### 8. Performance Testing
*   **`benchmark.py`**: A Python script to generate load on the API endpoints.
    *   Sends 100 requests to a specified endpoint.
    *   Calculates and prints min, max, average, and median latency.
    *   Useful for verifying AOP logging and endpoint tracking.

### 9. Database & Connection Pooling
*   **HikariCP Configuration**: Explicitly configured connection pool settings in `application.properties`:
    *   `minimum-idle=5`
    *   `maximum-pool-size=20`
    *   `auto-commit=true`
*   **H2 Database**: Configured to run in file mode (`./data/dbconnector`).

### 10. Endpoint Tracking Improvements
*   **Normalization**: `EndpointTrackingInterceptor` and `EndpointTrackingService` now strictly enforce lowercase storage for all endpoint URLs.
    *   This prevents duplicate stats for the same endpoint accessed with different casing (e.g., `/api/query/DEV/...` vs `/api/query/dev/...`).
    *   Ensures consistent aggregation of statistics on the dashboard.

## How to Run

1.  Ensure Java 24 is installed.
2.  Update `src/main/resources/application.properties` with your valid `client-id` and `client-secret`.
3.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  Access the application at `http://localhost:8080`.

## Running Benchmarks

To run the performance benchmark:
1.  Ensure the application is running.
2.  Execute the Python script:
    ```bash
    python3 benchmark.py
    ```

## Backup

To create a backup of the project:
```bash
./backup.sh
```
