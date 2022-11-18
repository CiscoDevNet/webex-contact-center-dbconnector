<h1 align="center">
    <a href="developer.webex-cx.com"></a>
</h1>

[![license](https://img.shields.io/github/license/ciscospark/spark-java-sdk.svg)](https://github.com/ciscospark/spark-java-sdk/blob/master/LICENSE)

# Webex Contact Center DB Connector

#### Connect SQL Servers with Webex Contact Center DB Connector

## Overview

![dbconnector1](/images/1.png)

> The Webex Contact Center DBConnector is a full stack java application that enables you to connect your SQL data sources from the Webex Contact Center HTTP(S) Nodes on Flow Designer.

![dbconnector2](/images/2.png)

> Once deployed on a virtual machine or containerized environment using a dockerfile, it exposes a web application that helps an administrator configure the data source, setup the REST API authentication, and test connectivity including running live queries.

**For more information on the walkthrough of the user interface and additional screenshots, refer the attached [Presentation - dbConnector walkthrough](./dbConnector.pptx)**

![dbconnector1](/images/3.png)

> Once the DB Connector app is configured and hosted along with a publicly reachable IP address and secure connectivity (HTTPS - with CA Signed Certificates) - the Flow Designer Node is configured to reach your database. For more information on Firewall configurations, refer the **[Security]()** section.

## Background of the Application

The application code is available for extension and configuration

## Prerequisites

- Java 1.8

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

## License

&copy; 2020 Cisco Systems, Inc. and/or its affiliates.
All Rights Reserved. See [LICENSE](LICENSE) for details.

## Change Log

| Date          | Title                 | Description                                                                                                                                                                                                     |
| ------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| November 2021 | Angular Upgrade       | Upgraded to angular 10.2.5                                                                                                                                                                                      |
| November 2021 | Connector Refactoring | Connectors now have separate pages so it is easier to add additional connectors: /app/connector <- main page /app/connector/mysql <- mysql connector page /app/connector/sqlserver <- sql server connector page |
| November 2021 | Refactoring           | Moved src/main/resources/application.yml out of the jar file and renamed application.yml to application.properties                                                                                              |
| November 2022 | Updates               | Readme updates, installation updates                                                                                                                                                                            |
