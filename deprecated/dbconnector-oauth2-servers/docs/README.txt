step 1. edit src/main/resources/application.yml
step 2. edit ./application.yml

step 3. mvn clean install
step 4. java -jar target/dbconnector-oauth2-servers-0.0.1-SNAPSHOT.jar





Requirements
   FQDN
   TLS -- if you need to create TLS certificate, refer to certbot.txt

app setting that require the code to be recompiled
src/main/resources/application.yml                                  <- edit
 key-store: /etc/letsencrypt/live/www.yourDomain.com/keystore.p12   <- update
 issuer-uri: https://www.yourDomain.com                             <- update
 client-id: your-client-id                                          <- update
 client-secret: your-client-secret                                  <- update
 token-uri: https://www.yourDomain.com/oauth2/token                 <- update
 issuer-uri: https://www.yourDomain.com                             <- update
 jwk-set-uri: https://www.yourDomain.com/oauth2/jwks                <- update

 userDetails                                                        <- update
   admin:   <- used to access this web site.                        <- update
   user:    <- use only if you want to other programs to make rest calls to this service
    

./application.yml                                                   <- edit/verify config
app setting that can be modified without recompiling the code



FYI.
Code
 ./com/cisco/oauth/config                                           <- Security code
 ./com/cisco/oauth/config/SecurityConfig.java
 ./com/cisco/oauth/config/JwtRequestFilter.java
 ./com/cisco/oauth/config/OAuth2ServerConfig.java
 ./com/cisco/oauth/controller                                       <- Security controllers
 ./com/cisco/oauth/controller/WebController.java
 ./com/cisco/oauth/controller/AuthenticationController.java
 ./com/cisco/oauth/controller/CustomErrorController.java
 ./com/cisco/app/DbconnectorOauth2Application.java                  <- Start the application
 ./com/cisco/app/util                                               <- Location for common code for the app
 ./com/cisco/app/util/Memory.java
 ./com/cisco/app/dbconnector/util                                   <- Location for common code for SQL
 ./com/cisco/app/dbconnector/util/Convertor.java
 ./com/cisco/app/dbconnector/util/Cypher2021.java
 ./com/cisco/app/dbconnector/util/DatabaseUtility.java
 ./com/cisco/app/dbconnector/controller                             <- Web page controllers
 ./com/cisco/app/dbconnector/controller/WebControllerRest.java
 ./com/cisco/app/dbconnector/controller/WebController.java
 ./com/cisco/app/dbconnector/controller/WebControllerUI.java
 ./com/cisco/app/dbconnector/controller/WebControllerWebexCC.java
 ./com/cisco/app/dbconnector/model                                  <- Models
 ./com/cisco/app/dbconnector/model/ConnectionPoolC3p0.java
 ./com/cisco/app/dbconnector/model/SqlServer.java
 ./com/cisco/app/dbconnector/model/NameValue.java
 ./com/cisco/app/dbconnector/model/DbConnection.java
 ./com/cisco/app/dbconnector/model/Oracle.java
 ./com/cisco/app/dbconnector/model/Authentication.java
 ./com/cisco/app/dbconnector/model/UserInfo.java
 ./com/cisco/app/dbconnector/model/BasicAuth.java
 ./com/cisco/app/dbconnector/model/Endpoint.java
 ./com/cisco/app/dbconnector/model/ConnectionPoolDb.java
 ./com/cisco/app/dbconnector/model/MySql.java
 ./com/cisco/app/dbconnector/service                                <- Storage service
 ./com/cisco/app/dbconnector/service/FileSystemInterface.java
 ./com/cisco/app/dbconnector/service/FileSystemLocalhost.java
 ./com/cisco/app/dbconnector/service/FileSystemAWSS3.java
 ./com/cisco/app/dbconnector/service/


Angular/html code location
 src/main/resources/static
 
 
 