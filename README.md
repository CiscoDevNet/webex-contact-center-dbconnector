# Getting Started

latest version -> https://github.com/CiscoDevNet/webex-contact-center-dbconnector/tree/main/com.cisco.webexcc.dbconnector

## v2.1.1 Documentation Update (2026-03-03)

- Added SQL + LDAP endpoint support across admin, test, and deploy workflows.
- Added LDAP deployment from DEV to UAT/PROD with overwrite confirmation.
- Updated help and About pages for SQL/LDAP parity and version `v2.1.1`.
- Fixed dashboard stats aggregation to include both SQL and LDAP endpoint traffic.
- Java 24 remains the recommended runtime/build target.

## Primary Documentation

- `README_installationguide.md`
- `README_userguide.md`
- `README_developer.md`
- `README_prompt.md`
- `README_databases.md`
- `CHANGELOG.md`

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.0/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.0/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

