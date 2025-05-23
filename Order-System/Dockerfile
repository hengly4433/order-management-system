# ── Build Stage ───────────────────────────────────────────────────────────────
FROM maven:3.9.0-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# copy Maven wrapper & pom for faster dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# go-offline to cache dependencies
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# copy sources and package
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ── Run Stage ────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine
LABEL maintainer="you@upskilldev.com"

# create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app
# copy the fat JAR
COPY --from=builder /workspace/target/Order-System-0.0.1-SNAPSHOT.jar ./order-system.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/order-system.jar"]
