# Disclosure Application

Basic VBOX Disclosure API sample using Java 25, Spring Boot 4.1.0, Azure SQL Database, JPA, validation, i18n, SLF4J, Azure Monitor OpenTelemetry configuration, global error handling, and MockMvc.

## Prerequisites
- JDK 25
- Maven 3.9+
- Azure SQL Database access

## Database
Create the database in Azure SQL Database, then configure the application with these environment variables:

- `DB_URL=jdbc:sqlserver://<server>.database.windows.net:1433;database=<database>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;`
- `DB_USERNAME=<sql-user>`
- `DB_PASSWORD=<password>`

These values are still supplied through `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` for Azure SQL deployment wiring.

## Run
```bash
mvn clean test
mvn spring-boot:run
```

## Endpoints
```bash
curl -H "Accept-Language: en" http://localhost:8080/api/disclosures/v1/greet

curl -X POST http://localhost:8080/api/disclosures/v1 \
  -H "Content-Type: application/json" \
  -d '{"referenceNumber":"DISC-1001","customerId":"CUST-101","description":"Basic disclosure"}'

curl -X POST http://localhost:8080/api/disclosures/v1/search \
  -H "Content-Type: application/json" \
  -d '{"referenceNumber":"DISC-1001","customerId":"CUST-101","status":"DRAFT","page":0,"pageSize":20}'

curl -X POST http://localhost:8080/api/disclosures/v1/disclosure-receipts \
  -H "Content-Type: application/json" \
  -d '{
    "workActionId":"WA-1001",
    "customerId":"CUST-101",
    "receiptType":"DISCLOSURE",
    "deliveryChannel":"EMAIL",
    "recipient":"customer@example.com",
    "receivedAt":"2025-01-15T10:00:00Z",
    "referenceNumber":"DISC-1001",
    "notes":"Disclosure delivered by email"
  }'
```

## Azure Monitor / OpenTelemetry
The POM includes `azure-monitor-opentelemetry-autoconfigure` as requested. Supply the Azure Monitor connection string using the platform-approved environment configuration. Do not commit secrets.

## Notes
- DTOs are separate from domain and JPA entity representations.
- Search is POST `/search`; create is POST on the resource root.
- Errors use the Section 13 standard response fields and include a trace ID when an active OpenTelemetry span exists.
- For production, replace `ddl-auto: update` with Liquibase migrations.
