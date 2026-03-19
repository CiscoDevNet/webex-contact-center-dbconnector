# KEYTOOL Certificate Import Guide

This guide explains how to import the certificate file YOUR.crt into the Java truststore (cacerts) located at /usr/java/latest/lib/security/cacerts.

Use this process when a Java application must trust an internal or private TLS certificate.

## What You Need

- Certificate file name: YOUR.crt
- Truststore path: /usr/java/latest/lib/security/cacerts
- Access level: sudo/root access is usually required to update system-level cacerts
- Java keytool command available in PATH

## Important Notes Before You Start

- The default cacerts password is often changeit unless your environment changed it.
- Importing into system cacerts affects all Java apps using that JDK.
- Always back up cacerts before importing certificates.

## 1. Confirm keytool and certificate file

Run these checks first:

    which keytool
    keytool -help

If the certificate is in your current directory:

    ls -l YOUR.crt

If it is in another location, use the full path in later commands.

## 2. Back up the existing cacerts

Create a timestamped backup before making changes:

    sudo cp /usr/java/latest/lib/security/cacerts /usr/java/latest/lib/security/cacerts.bak.$(date +%Y%m%d_%H%M%S)

This allows quick rollback if needed.

## 3. Inspect certificate details before import

Review subject, issuer, and validity:

    keytool -printcert -file YOUR.crt

Confirm this is the correct certificate for your target endpoint.

## 4. Choose a stable alias

Use a clear alias name so future updates are easy to manage.

Recommended alias:

    webexapi-hdis-com

## 5. Import certificate into cacerts

Import command:

    sudo keytool -importcert \
      -alias webexapi-hdis-com \
      -file YOUR.crt \
      -keystore /usr/java/latest/lib/security/cacerts

When prompted:

- Enter keystore password (often changeit)
- Type yes to trust the certificate

Non-interactive version (if approved by your security policy):

    sudo keytool -importcert -noprompt \
      -alias webexapi-hdis-com \
      -file YOUR.crt \
      -keystore /usr/java/latest/lib/security/cacerts \
      -storepass changeit

## 6. Verify import succeeded

Check the alias exists:

    sudo keytool -list -keystore /usr/java/latest/lib/security/cacerts -alias webexapi-hdis-com

Get verbose certificate details from truststore:

    sudo keytool -list -v -keystore /usr/java/latest/lib/security/cacerts -alias webexapi-hdis-com

## 7. Test your Java connection

After import, rerun your Java client/app that connects to the TLS endpoint and confirm SSL handshake succeeds.

If your application uses a different JDK, repeat import for that JDK's cacerts or configure app-level truststore explicitly.

## Troubleshooting

### Alias already exists

You may see an error that the alias already exists.

Options:

- Verify existing alias details:

    sudo keytool -list -v -keystore /usr/java/latest/lib/security/cacerts -alias webexapi-hdis-com

- Replace by deleting then re-importing:

    sudo keytool -delete -alias webexapi-hdis-com -keystore /usr/java/latest/lib/security/cacerts
    sudo keytool -importcert -alias webexapi-hdis-com -file YOUR.crt -keystore /usr/java/latest/lib/security/cacerts

### Keystore was tampered with, or password incorrect

- Re-enter the correct truststore password.
- Confirm you are targeting the correct cacerts file.

### Permission denied

- Use sudo.
- Confirm you have rights to write to /usr/java/latest/lib/security/cacerts.

## Rollback Procedure

If something goes wrong, restore backup:

    sudo cp /usr/java/latest/lib/security/cacerts.bak.YYYYMMDD_HHMMSS /usr/java/latest/lib/security/cacerts

Replace YYYYMMDD_HHMMSS with your actual backup suffix.

## Optional: Safer App-Local Truststore (Recommended for production control)

Instead of modifying global cacerts, create a dedicated truststore for one app:

    keytool -importcert -alias webexapi-hdis-com -file YOUR.crt -keystore ./app-truststore.jks

Then run Java with:

    -Djavax.net.ssl.trustStore=/path/to/app-truststore.jks
    -Djavax.net.ssl.trustStorePassword=YOUR_PASSWORD

This approach limits impact to a single application.
