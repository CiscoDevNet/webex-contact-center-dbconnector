<h1 align="center">
    <a href="developer.webex-cx.com"></a>
</h1>

[![license](https://img.shields.io/github/license/ciscospark/spark-java-sdk.svg)](https://github.com/ciscospark/spark-java-sdk/blob/master/LICENSE)

# Webex Contact Center DB Connector

### Configuration Video 

##### [Watch Now: WebexCC DB Connector Installation & Walkthrough](https://app.vidcast.io/share/b65cf961-def5-41bf-a9fb-cd7c88eb61b3)

### Connect SQL Servers with Webex Contact Center DB Connector

- [Overview](#overview)
- [Background](#background-of-the-application)
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Security & Production Deployment](#security--production-deployment)
- [Support](#support)
- [Questions?](#questions)
- [Change Log](#change-log)
- [License](#license)

## Overview

![dbconnector1](/images/1.png)

> The Webex Contact Center DBConnector is a full stack java application that enables you to connect your SQL data sources from the Webex Contact Center HTTP(S) Nodes on Flow Designer.

![dbconnector2](/images/2.png)

> Once deployed on a virtual machine or containerized environment using a dockerfile, it exposes a web application that helps an administrator configure the data source, setup the REST API authentication, and test connectivity including running live queries.

**For more information on the walkthrough of the user interface and additional screenshots, refer the attached [Presentation - dbConnector walkthrough](./dbConnector.pptx)**

![dbconnector1](/images/3.png)

> Once the DB Connector app is configured and hosted along with a publicly reachable IP address and secure connectivity (HTTPS - with CA Signed Certificates) - the Flow Designer Node is configured to reach your database. For more information on Firewall configurations, refer the **[Security & Production Deployment](#security--production-deployment)** section.

## Background

The Webex Contact Center DB Connector is a full stack application that enables you to manage your SQL data source connectivity with Webex Contact Center.

It is powered by a Spring Boot backend that enables JDBC to authenticated REST API interfaces that are configurable by the administrator for GET/PUT/POST use cases based on the SQL Queries configured.

The frontend is built using Angular.

The application code is available open source on GitHub for extension along with accompanying Readmes on hosting options.

For the DB Connector to work effectively here is the 4 step process:

1. Download, Build and Package the DB Connector JAR file or container - ready to be deployed and run on a production VM or container.

2. Configure the properties of the production environment, including the Firewall settings, Reverse Proxy for reachability from the Internet, and CA Signed Certificates for HTTPS connectivity.

3. Test using the DBConnector "TEST SQL Connectivity" and test the REST API calls using Postman.
4. Configure your HTTP Node inside Flow designer to begin using the DB Connector application to query your SQL data sources.

## Prerequisites

- Java 1.9+

`$ java --version`

- [Apache Maven](https://maven.apache.org/)

`$ mvn --version`

- [Angular](https://angular.io/)

`$ ng version`

## Usage

1. Download the source code by cloning the repo or downloading the zip from https://github.com/CiscoDevNet/webex-contact-center-dbconnector

2. `$ unzip webex-contact-center-dbconnector-main.zip`

3. `$ cd webex-contact-center-dbconnector-main`

4. Request for a Client ID & Client Secret pair from https://developer.webex-cx.com. Follow the steps mentioned in the Readme on how to get started with the Webex CC APIs and obtain a client ID / Client Secret:

5. Update your `application.yml` with the credentials

Under `src > main > java > application.yml`

6. Install the Dependencies for the frontend:

`$ npm i @angular/cli@8`

7. Install dependencies and package the JAR:

`$ mvn clean install`

8. Run the application

`$ java -jar target/dbConnectorGitHub-0.0.2.jar`

9. Browse to the application at: http://localhost:8080/

10. Login with Webex and follow the detailed walkthrough at: [DB Connector - Walkthrough](./dbConnector.pptx)

## Security & Production Deployment

For DB Connector to work effectively in production behind your premise firewall, there are 4 requirements:

1. **Authentication:** Authentication of the REST API endpoint, configured in the Spring Security settings: By default, username and password authentication is supported, along with WebexCC OAuth2 for the administration portal. This is out of box and requires a configuration in the YAML settings.

2. **Firewall Settings:** Allow list Webex Contact Center IP Addresses as the SRC (Source) IP Address. The block of IPs are listed in the Security Document.
   Link: **https://help.webex.com/en-us/article/3srgv1/Security-Settings-for-Webex-Contact-Center**

3. **Reverse Proxy:** A reverse proxy is required for the DB Connector endpoint to be publicly reachable on the Cloud. This ensures that Webex Contact Center is able to reach your application over the public internet.

4. **CA Signed Certificates:** The Web application needs to be hosted with a CA signed certificate for this to work effectively. This is a pre-requisite for HTTPS traffic between the HTTPClient (HTTP Node) on Flow Designer and the DB Connector application.

## Support

> The DB Connector application is available for free, and the open source code is shared to be able to extend the functionality to support more data sources.

> Once built and deployed, the support of the DB Connector application needs to be handled by the partner or customer hosting this application on-premise or in the cloud.

## Questions?

For Questions on building the application or best practices during deployment, please use the Cisco Developer Community Page:

Need Help? Visit the **[Webex Contact Center APIs Developer Community](https://community.cisco.com/t5/contact-center/bd-p/j-disc-dev-contact-center)**

Refer: **[How to Ask a Question or Initiate a Discussion](https://community.cisco.com/t5/contact-center/webex-contact-center-apis-developer-community-and-support/m-p/4558270)**

## Change Log

| Date          | Title                 | Description                                                                                                                                                                                                     |
| ------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| November 2021 | Angular Upgrade       | Upgraded to angular 10.2.5                                                                                                                                                                                      |
| November 2021 | Connector Refactoring | Connectors now have separate pages so it is easier to add additional connectors: /app/connector <- main page /app/connector/mysql <- mysql connector page /app/connector/sqlserver <- sql server connector page |
| November 2021 | Refactoring           | Moved src/main/resources/application.yml out of the jar file and renamed application.yml to application.properties                                                                                              |
| November 2022 | Updates               | Readme updates, installation updates                                                                                                                                                                            |

## License

&copy; 2020 Cisco Systems, Inc. and/or its affiliates.
All Rights Reserved. See [LICENSE](LICENSE) for details.
