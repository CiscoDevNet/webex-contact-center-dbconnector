# Recommended Prompts for Project Recreation

This document outlines a sequence of prompts that could be used to recreate the current state of the Webex Contact Center DB Connector project. These prompts cover the setup, authentication, security, and utility scripts.

## 1. Backup and Maintenance Script
> "Create a bash script named `backup.sh` that performs the following actions:
> 1. Defines source and destination directories.
> 2. Runs `mvn clean` to remove build artifacts.
> 3. Zips the entire project (excluding `.git`) into a timestamped file in a sibling directory named `_zip`.
> 4. After zipping, runs `mvn clean package -DskipTests` to ensure the project builds correctly."

## 2. Project Dependencies and Configuration
> "Update the `pom.xml` file to use Spring Boot version 3.4.0. Add the following dependencies:
> *   `spring-boot-starter-web`
> *   `spring-boot-starter-security`
> *   `spring-boot-starter-oauth2-client`
> *   `spring-boot-starter-thymeleaf`
> *   `spring-boot-starter-test` (scope: test)"

## 3. Webex OAuth2 Configuration
> "Configure `src/main/resources/application.properties` for Webex OAuth2 authentication. Use the following settings:
> *   **Client ID**: `PLACE_HOLDER_1`
> *   **Client Secret**: `PLACE_HOLDER_2`
> *   **Scopes**: `spark:people_read`
> *   **Authorization Grant Type**: `authorization_code`
> *   **Redirect URI**: `{baseUrl}/login/oauth2/code/{registrationId}`
> *   **Provider URLs**:
>     *   Auth: `https://webexapis.com/v1/authorize`
>     *   Token: `https://webexapis.com/v1/access_token`
>     *   User Info: `https://webexapis.com/v1/people/me`
>     *   User Name Attribute: `displayName`"

## 4. Security Configuration
> "Create a `SecurityConfig` class to configure the security filter chain.
> *   Allow public access to `/`, `/login**`, and `/error**`.
> *   Require authentication for all other requests.
> *   Configure `oauth2Login` to redirect to `/welcome` upon successful login.
> *   Configure logout to redirect to `/`."

## 5. Web Controller and UI
> "Create a `WebController` and the following Thymeleaf templates:
> 1.  **`index.html`**: A landing page with a 'Login with Webex' button.
> 2.  **`welcome.html`**: A page displaying 'Welcome, [User Name]!' and a link to the dashboard.
> 3.  **`home.html`**: A dashboard page displaying the user's raw attributes for debugging.
>
> Update the `WebController` to:
> *   Serve `index.html` at `/`. If the user is already authenticated, redirect them to `/welcome`.
> *   Serve `welcome.html` at `/welcome` and inject the user's display name.
> *   Serve `home.html` at `/home` and inject the user's attributes."

## 6. Aspect Oriented Programming (AOP) Logging
> "Implement an Aspect using Spring AOP to log the execution time of API methods.
> 1.  Add `spring-boot-starter-aop` dependency to `pom.xml`.
> 2.  Create a `LoggingAspect` class annotated with `@Aspect` and `@Component`.
> 3.  Define an `@Around` advice targeting all public methods in `ApiController`.
> 4.  Log the method name upon entry.
> 5.  Calculate and log the execution time in milliseconds upon exit.
> 6.  Configure `logback-spring.xml` to abbreviate package names (e.g., `c.c.w.d.aspect`) for cleaner logs."

## 7. Performance Benchmarking Script
> "Create a Python script named `benchmark.py` to test the API performance.
> 1.  Use the `requests` library to send 100 HTTP GET requests to a specific API endpoint (e.g., `http://127.0.0.1:8080/api/query/DEV/mytesttable2`).
> 2.  Measure the latency of each request.
> 3.  Calculate and print statistics: Total duration, Success/Failure counts, Average, Median, Min, and Max latency."

## 8. Database Connection Pooling
> "Configure HikariCP connection pool settings in `application.properties` to optimize performance and stability:
> *   `spring.datasource.hikari.minimum-idle=5`
> *   `spring.datasource.hikari.maximum-pool-size=20`
> *   `spring.datasource.hikari.auto-commit=true`"

## 9. Endpoint Tracking Normalization
> "Ensure consistent tracking of API endpoints by enforcing lowercase URLs.
> 1.  Update `EndpointTrackingInterceptor` to convert the request URI to lowercase before passing it to the tracking service.
> 2.  Update `EndpointTrackingService` to strictly enforce lowercase conversion when saving `EndpointExecution` records to the database.
> 3.  This prevents duplicate entries for the same endpoint accessed with different casing (e.g., `/api/query/DEV/...` vs `/api/query/dev/...`)."

## 6. Startup Debugging
> "Modify the main `DbConnectorApplication` class to implement `CommandLineRunner`. Inject the `redirect-uri`, `server.port`, and `server.servlet.context-path` properties. In the `run` method, calculate and print the expected Base URL and the full Redirect URI to the console to assist with Webex integration configuration."
