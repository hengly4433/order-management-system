# ── Build Stage ───────────────────────────────────────────────────────────────
FROM maven:3-eclipse-temurin-21-jammy AS java-builder
WORKDIR /workspace

# copy the Maven wrapper (and make it executable) + pom for faster dependency caching
COPY --chmod=0755 mvnw .
COPY .mvn .mvn
COPY pom.xml .

# go-offline to cache dependencies (no chmod needed here any more)
RUN ./mvnw dependency:go-offline -B

# copy sources and package
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ── Run Stage ────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy
LABEL maintainer="you@upskilldev.com"

# create non-root user & group
RUN groupadd -r spring \
 && useradd -r -g spring -d /app -s /sbin/nologin spring
USER spring

WORKDIR /app
# copy the fat JAR
COPY --from=java-builder /workspace/target/Order-System-0.0.1-SNAPSHOT.jar ./order-system.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/order-system.jar"]
