# DISCLOSURE_RECEIPT_API_FORWARD_ENGINEERING.md

## Project
VBOX Disclosure Service

## Objective
Generate a Spring Boot 4.1.x REST API using Java 25 for creating Disclosure Receipts.

## Technology Stack
- Java 25
- Spring Boot 4.1.x
- Maven
- Azure SQL
- Spring Data JPA
- Spring Validation
- OpenAPI / Swagger
- JUnit 5
- MockMvc
- Lombok

---

# Use Case: Create Disclosure Receipt

## Endpoint

```http
POST /api/disclosures/v1/disclosure-receipts
```

## Request

```json
{
  "workActionId": "WA10001",
  "customerId": "12345",
  "receiptType": "DISCLOSURE",
  "deliveryChannel": "EMAIL",
  "recipient": "customer@example.com",
  "receivedAt": "2026-09-03T10:30:00Z",
  "referenceNumber": "DISC-REF-10001",
  "notes": "Disclosure receipt created after successful delivery"
}
```

## Business Rules
- workActionId is mandatory
- customerId is mandatory
- receiptType is mandatory
- deliveryChannel is mandatory
- recipient is mandatory
- receivedAt is mandatory
- EMAIL delivery requires valid email recipient
- receivedAt cannot be future date
- Duplicate receipt not allowed for workActionId + receiptType + referenceNumber

## Request DTO

```java
public record CreateDisclosureReceiptRequest(
    String workActionId,
    String customerId,
    ReceiptType receiptType,
    DeliveryChannel deliveryChannel,
    String recipient,
    Instant receivedAt,
    String referenceNumber,
    String notes
){}
```

## Response DTO

```java
public record CreateDisclosureReceiptResponse(
    String disclosureReceiptId,
    String workActionId,
    String customerId,
    DisclosureReceiptStatus status
){}
```

## Success Response

```json
{
  "disclosureReceiptId": "DR10001",
  "workActionId": "WA10001",
  "customerId": "12345",
  "status": "CREATED"
}
```

## Application Layer
Generate:
- CreateDisclosureReceiptUseCase
- CreateDisclosureReceiptCommand
- CreateDisclosureReceiptResult

## Domain Layer
Generate:
- DisclosureReceipt
- DisclosureReceiptRepository
- ReceiptType
- DeliveryChannel
- DisclosureReceiptStatus

## Infrastructure Layer
Generate:
- DisclosureReceiptJpaEntity
- SpringDataDisclosureReceiptRepository
- DisclosureReceiptRepositoryAdapter
- Flyway Migration
- Azure SQL Configuration

## Controller
Generate:
- DisclosureReceiptController

```java
@PostMapping("/disclosure-receipts")
public ResponseEntity<CreateDisclosureReceiptResponse> createDisclosureReceipt(
        @Valid @RequestBody CreateDisclosureReceiptRequest request)
```

## Exception Handling 
## Standard Error Response Format

This API uses the standard application envelope for all error responses:

```json
{
  "status": "ERROR",
  "statusCode": 400,
  "message": "Validation failed",
  "errors": [
    {
      "code": "ERR-VALIDATION-WORKACTIONID",
      "message": "workActionId is mandatory"
    }
  ],
  "warnings": [],
  "data": {}
}
```

### Response Format

```json
 { 
  "status": "ERROR", 
  "statusCode": , 
  "errors": [], 
  "warnings": [], 
  "data": {} 
} 

Validation Error -> HTTP 400
Work Action Not Found -> HTTP 404
Duplicate Receipt -> HTTP 409

## Tests
Generate:
- DisclosureReceiptControllerTest
- CreateDisclosureReceiptUseCaseTest
- DisclosureReceiptRepositoryTest

Test Scenarios:
- Success
- Invalid Email
- Missing Mandatory Fields
- Duplicate Receipt
- Work Action Not Found
- Future Date Validation

## Quality Gates
- Use HTTP 200 OK for success
- Use Java Records for DTOs
- Constructor Injection Only
- No Field Injection
- mvn clean test must pass
- Do not expose JPA entities via API

## Generation Instruction
Generate complete production-ready code from this specification.
Do not generate TODOs or pseudocode.
Run mvn clean test and fix compilation issues before completion.
