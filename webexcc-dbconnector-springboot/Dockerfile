FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} dbconnector.jar
COPY application.yml application.yml

ENTRYPOINT ["java","-jar","/dbconnector.jar"]

# docker build -t target/dbconnector-springboot .
# docker run -p 8080:8080 target/dbconnector-springboot

# docker build -t target/dbconnector-springboot .;docker run -p 8080:8080 target/dbconnector-springboot
