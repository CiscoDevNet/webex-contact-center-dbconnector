FROM maven:3.9.11-eclipse-temurin-24 AS builder
WORKDIR /build

# Copy build metadata first for better layer caching.
COPY pom.xml ./
COPY .mvn ./.mvn
COPY mvnw ./
RUN chmod +x mvnw

# Copy source and build the runnable Spring Boot jar.
COPY src ./src
RUN ./mvnw -DskipTests clean package

FROM eclipse-temurin:24-jdk
WORKDIR /app

RUN mkdir -p /app/config

COPY --from=builder /build/target/*.jar /app/app.jar

EXPOSE 8080

# Persist the embedded H2 data directory.
VOLUME ["/app/data"]

ENTRYPOINT ["java", "-jar", "/app/app.jar"]