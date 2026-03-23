# Docker Run Guide

This document explains everything needed to build and run the Webex Contact Center DB Connector with Docker.

## 1. What Is Included

The project contains:

- `Dockerfile` (multi-stage build using Java 24)
- `docker-compose.yml` (service orchestration, environment variables, volume, and restart policy)
- `.dockerignore` (optimized Docker build context)

## 2. Prerequisites

Install the following on your machine:

- Docker Desktop (macOS/Windows), or
- Docker Engine + Docker Compose plugin (Linux)

Verify installation:

```bash
docker --version
docker compose version
```

If either command fails, install or start Docker before continuing.

## 3. Required Configuration Values

The app requires these values (already present in `src/main/resources/application.properties` and also supplied by `docker-compose.yml`):

```properties
spring.security.oauth2.client.registration.webex.client-id=YOUR_WEBEX_CLIENT_ID
spring.security.oauth2.client.registration.webex.client-secret=YOUR_WEBEX_CLIENT_SECRET
spring.security.oauth2.client.registration.webex.redirect-uri=http://localhost:8080/login/oauth2/code/webex
spring.datasource.username=sa
spring.datasource.password=password
```

## 4. Build and Start

From the project root (`com.cisco.webexcc.dbconnector`), run:

```bash
docker compose up --build -d
```

What this does:

- Builds the app image from `Dockerfile`
- Starts container `webex-dbconnector`
- Exposes app port `8080`
- Mounts host path `${HOME}/dbconnector/data` to `/app/data`
- Loads datasource URL override from `config/container-datasource.properties`

## 5. Confirm It Is Running

Check container status:

```bash
docker compose ps
```

Check logs:

```bash
docker compose logs -f
```

Open the app:

```text
http://localhost:8080
```

## 6. Stop and Cleanup

Stop services:

```bash
docker compose down
```

Stop services and remove compose resources:

```bash
docker compose down -v
```

## 7. Data Persistence

H2 file-based data persists between restarts because Compose uses:

- Host path: `${HOME}/dbconnector/data`
- Container path: `/app/data`

To start with a clean database, remove files under `${HOME}/dbconnector/data`.

## 8. Rebuild After Code Changes

When source code or dependencies change:

```bash
docker compose up --build -d
```

If needed, force full rebuild without cache:

```bash
docker compose build --no-cache
docker compose up -d
```

## 9. Optional: Override Environment Values

To run with different values without editing `docker-compose.yml`, use a Compose override file.

Example `docker-compose.override.yml`:

```yaml
services:
  dbconnector:
    environment:
      SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_WEBEX_CLIENT_ID: "your-real-client-id"
      SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_WEBEX_CLIENT_SECRET: "your-real-client-secret"
      SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_WEBEX_REDIRECT_URI: "http://localhost:8080/login/oauth2/code/webex"
      SPRING_DATASOURCE_USERNAME: "sa"
      SPRING_DATASOURCE_PASSWORD: "password"
```

Then run as usual:

```bash
docker compose up --build -d
```

## 10. Troubleshooting

### `docker: command not found`
Docker is not installed or not in PATH. Install Docker Desktop (macOS/Windows) or Docker Engine + Compose plugin (Linux).

### Port 8080 already in use
Free the port or change host mapping in `docker-compose.yml`.

Example:

```yaml
ports:
  - "8081:8080"
```

Then access:

```text
http://localhost:8081
```

### OAuth login redirect mismatch
Ensure the configured Webex redirect URI matches:

```text
http://localhost:8080/login/oauth2/code/webex
```

(and the host port you actually use).

### Start fresh
If state is corrupted or credentials changed:

```bash
docker compose down -v
docker compose up --build -d
```

## 11. Useful Commands (Quick Reference)

```bash
# Build and run
docker compose up --build -d

# Status
docker compose ps

# Logs
docker compose logs -f

# Restart
docker compose restart

# Stop
docker compose down

# Stop and remove data
docker compose down -v
```

## v2.1.2 Documentation Update (2026-03-23)

- Added LDAP Help button coverage across LDAP admin pages for consistency:
  - `/admin/ldap` (statements list)
  - `/admin/ldap/add` and `/admin/ldap/edit/{id}` (add/edit form)
  - `/admin/ldap/deploy/{id}` (deploy form)
- Added new LDAP help content pages:
  - `/help_ldap.html`
  - `/help_ldap_edit.html`
