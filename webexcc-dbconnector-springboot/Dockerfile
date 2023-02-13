FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} dbconnector.jar
COPY application.properties application.properties

ENTRYPOINT ["java","-jar","/dbconnector.jar"]

# docker build -t target/dbconnector-server .
# docker run -p 8080:8080 target/dbconnector-server

# docker build -t target/dbconnector-server .;docker run -p 8080:8080 target/dbconnector-server
