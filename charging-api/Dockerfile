FROM maven:3.9.11-eclipse-temurin-8 AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:8-jre

WORKDIR /app

COPY --from=builder /build/xingchuan-admin/target/*.jar app.jar

RUN mkdir -p /app/adminLogs /app/uploadPath /app/certs && \
    adduser --disabled-password --gecos '' appuser && \
    chown -R appuser:appuser /app

USER appuser
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:--Xms256m -Xmx1024m -XX:+UseG1GC} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
