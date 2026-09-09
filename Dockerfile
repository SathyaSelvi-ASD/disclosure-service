# -----------------------------------------
# Stage 1 - Build
# -----------------------------------------
FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# -----------------------------------------
# Stage 2 - Runtime
# -----------------------------------------
FROM eclipse-temurin:25-jre

RUN groupadd -r disclosure && \
    useradd -r -g disclosure disclosure

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R disclosure:disclosure /app

USER disclosure

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]