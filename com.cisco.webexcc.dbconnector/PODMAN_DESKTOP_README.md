# Podman Desktop Deployment Guide

This guide explains how to run the Webex Contact Center DB Connector in Podman Desktop.

## 1. Prerequisites

- Podman Desktop installed
- Podman machine initialized and running

In Podman Desktop:

1. Open Podman Desktop.
2. Ensure the Podman engine is started.
3. Open the project folder:
   - `com.cisco.webexcc.dbconnector`

## 2. Files Used for Podman

This repository includes:

- `Containerfile` for image build
- `podman-compose.yml` for app deployment
- `config/container-datasource.properties` for container datasource URL override

## 3. Required Property Values

The deployment sets these required values via environment variables:

```properties
spring.security.oauth2.client.registration.webex.client-id=YOUR_WEBEX_CLIENT_ID
spring.security.oauth2.client.registration.webex.client-secret=YOUR_WEBEX_CLIENT_SECRET
spring.security.oauth2.client.registration.webex.redirect-uri=http://localhost:8080/login/oauth2/code/webex
spring.datasource.username=sa
spring.datasource.password=password
```

The datasource URL is loaded from an external file mounted into the container:

```properties
spring.datasource.url=jdbc:h2:file:/app/data/dbconnector
```

File path to edit:

`config/container-datasource.properties`

They are also present in `src/main/resources/application.properties`.

## 4. Deploy with Podman Compose

From the project root, run:

```bash
podman compose -f podman-compose.yml up --build -d
```

This will:

- Build image `localhost/webex-dbconnector:latest`
- Run container `webex-dbconnector`
- Expose port `8080`
- Persist H2 data to `${HOME}/dbconnector/data` via bind mount `${HOME}/dbconnector/data:/app/data:Z`

## 5. Verify Deployment

Check running containers:

```bash
podman ps
```

Check logs:

```bash
podman logs -f webex-dbconnector
```

Open in browser:

```text
http://localhost:8080
```

## 6. Stop Deployment

```bash
podman compose -f podman-compose.yml down
```

Remove deployment and persistent data:

```bash
podman compose -f podman-compose.yml down -v
```

## 7. Podman Desktop UI Flow (Optional)

You can also deploy from Podman Desktop UI:

1. Go to Pods/Containers.
2. Use Compose / Play Compose option.
3. Select `podman-compose.yml`.
4. Start the compose stack.

## 8. Troubleshooting

### `podman: command not found`
Install Podman Desktop, then restart your terminal.

### Port already in use
Update host port in `podman-compose.yml`:

```yaml
ports:
  - "8081:8080"
```

Then browse to `http://localhost:8081`.

### SELinux volume permission issue (Linux)
The compose file already includes `:Z` on the volume mount:

```yaml
- ${HOME}/dbconnector/data:/app/data:Z
```

Keep that suffix for SELinux-enabled hosts.

### MySQL connection fails from container
If your JDBC URL uses `127.0.0.1` inside the container, the app tries to connect to MySQL in the container itself, not on your host machine.

Use this host alias instead:

```properties
jdbc:mysql://host.containers.internal:3306/MyTestDb
```

Also ensure MySQL is listening on a non-loopback interface. If it is bound only to `127.0.0.1:3306`, the container cannot reach it.

Quick checks:

```bash
# Host: verify MySQL listening address
lsof -nP -iTCP:3306 -sTCP:LISTEN

# Container: verify host alias resolves
podman exec webex-dbconnector getent hosts host.containers.internal
```

## v2.1.2 Documentation Update (2026-03-23)

- Added LDAP Help button coverage across LDAP admin pages for consistency:
  - `/admin/ldap` (statements list)
  - `/admin/ldap/add` and `/admin/ldap/edit/{id}` (add/edit form)
  - `/admin/ldap/deploy/{id}` (deploy form)
- Added new LDAP help content pages:
  - `/help_ldap.html`
  - `/help_ldap_edit.html`
