# Changelog

All notable changes to this project are documented in this file.

## v2.1.1 - 2026-03-03

### Added
- LDAP deploy workflow for DEV to UAT/PROD with overwrite confirmation.
- Unified test console support for SQL and LDAP endpoint execution.
- Route compatibility handling for LDAP deploy URL variants.
- Color-coded target environment indicator on LDAP deploy form.
- SQL-style icon actions on LDAP statements page.

### Changed
- About page content updated for SQL + LDAP feature parity.
- About page version badge updated to `v2.1.1`.
- Help pages updated for both SQL and LDAP workflows:
  - `help_test.html`
  - `help_deploy.html`
- Documentation refresh across root and module Markdown files.

### Fixed
- `/home` dashboard stats now aggregate SQL and LDAP endpoint activity.
- LDAP connection selection and save logic now supports UAT/PROD correctly.
- LDAP deploy 404 behavior reduced by supporting additional route forms.

### Runtime
- Java 24 is the documented and validated runtime baseline.
