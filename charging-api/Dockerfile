# Build stage
FROM docker.1ms.run/library/maven:3.9.11-eclipse-temurin-8 AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests -q

# Runtime stage
FROM docker.1ms.run/library/eclipse-temurin:8-jre

WORKDIR /app

# Copy JAR from build stage
COPY --from=builder /build/xingchuan-admin/target/*.jar app.jar

# Create non-root user
RUN adduser --disabled-password --gecos '' appuser && \
    chown appuser:appuser app.jar

USER appuser
EXPOSE 8080

# Start application
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:--Xms256m -Xmx1024m -XX:+UseG1GC} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
