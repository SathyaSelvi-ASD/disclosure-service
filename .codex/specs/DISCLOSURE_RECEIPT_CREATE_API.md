# Disclosure Receipt Create API

## API

- **Method:** POST
- **Path:** `/api/disclosures/v1/disclosure-receipts`

---

# Request

## Request Type

`CreateDisclosureReceiptRequest`

### Request Body

```json
{
  "workActionId": "WA10001",
  "customerId": "12345",
  "receiptType": "DISCLOSURE",
  "deliveryChannel": "EMAIL",
  "recipient": "[REDACTED_EMAIL_ADDRESS_1]",
  "receivedAt": "2026-09-03T10:30:00Z",
  "referenceNumber": "DISC-REF-10001",
  "notes": "Disclosure receipt created after successful delivery"
}
```

---

# Response

## Success Response

- **HTTP Status:** `200 OK`
- **Response Type:** `ApiResponse<CreateDisclosureReceiptResponse>`

### Response Structure

```text
ApiResponse
├── status
├── statusCode
├── message
├── data
│   └── CreateDisclosureReceiptResponse
├── warnings
└── errors
```

### Response Body

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "message": "Disclosure receipt created successfully.",
  "data": {
    "disclosureReceiptId": "DR10001",
    "workActionId": "WA10001",
    "customerId": "12345",
    "status": "CREATED"
  },
  "warnings": [],
  "errors": []
}
```

---

# Business Requirement

Create a Disclosure Receipt for a completed customer disclosure delivery.

The API records receipt information including delivery channel, recipient, received timestamp, and reference information for auditing and compliance.

---

# Validation

## Mandatory Fields

- workActionId
- customerId
- receiptType
- deliveryChannel
- recipient
- receivedAt

## Business Validation

- workActionId must exist.
- receivedAt cannot be a future timestamp.
- Duplicate receipts are not allowed for the combination of:
    - workActionId
    - receiptType
    - referenceNumber
- When deliveryChannel is `EMAIL`, recipient must be a valid email address.

---

# Business Rules

| Rule | Description |
|-------|-------------|
| BR-001 | Work Action must exist before creating a receipt. |
| BR-002 | Receipt Type is mandatory. |
| BR-003 | Delivery Channel is mandatory. |
| BR-004 | Recipient is mandatory. |
| BR-005 | Email delivery requires a valid email address. |
| BR-006 | receivedAt cannot be a future date/time. |
| BR-007 | Duplicate receipts are not permitted for the same Work Action, Receipt Type, and Reference Number. |

---

# Success Response

HTTP Status

```
200 OK
```

Example

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "message": "Disclosure receipt created successfully.",
  "data": {
    "disclosureReceiptId": "DR10001",
    "workActionId": "WA10001",
    "customerId": "12345",
    "status": "CREATED"
  },
  "warnings": [],
  "errors": []
}
```

---

# Error Handling

This API uses the existing **RFC 9457 Problem Details** implementation.

## Validation Error

**HTTP 400 Bad Request**

Examples include:

- Missing mandatory fields
- Invalid email format
- Future receivedAt timestamp

Example

```json
{
  "type": "https://example.com/problems/validation-error",
  "title": "Validation Failed",
  "status": 400,
  "detail": "Request validation failed.",
  "instance": "/api/disclosures/v1/disclosure-receipts"
}
```

---

## Work Action Not Found

**HTTP 404 Not Found**

Returned when the supplied Work Action does not exist.

---

## Duplicate Receipt

**HTTP 409 Conflict**

Returned when a receipt already exists for the same:

- workActionId
- receiptType
- referenceNumber

---

## Internal Server Error

**HTTP 500 Internal Server Error**

Returned for unexpected system failures.

---

# Application Components

Generate

- CreateDisclosureReceiptUseCase
- CreateDisclosureReceiptCommand
- CreateDisclosureReceiptResult

---

# Domain Components

Generate

- DisclosureReceipt
- DisclosureReceiptRepository
- ReceiptType
- DeliveryChannel
- DisclosureReceiptStatus

---

# Infrastructure Components

Generate

- DisclosureReceiptJpaEntity
- SpringDataDisclosureReceiptRepository
- DisclosureReceiptRepositoryAdapter
- Flyway Migration
- Azure SQL Configuration

---

# Controller

Generate

```text
@PostMapping("/disclosure-receipts")
public ResponseEntity<ApiResponse<CreateDisclosureReceiptResponse>>
createDisclosureReceipt(
    @Valid @RequestBody CreateDisclosureReceiptRequest request)
```

---

# Testing

Generate

- DisclosureReceiptControllerTest
- CreateDisclosureReceiptUseCaseTest
- DisclosureReceiptRepositoryTest

## Test Scenarios

### Controller

- Successful creation
- Validation failure
- Invalid email
- Future receivedAt
- Duplicate receipt
- Work Action not found

### Use Case

- Creates receipt successfully
- Validates duplicate receipt
- Validates Work Action existence
- Validates receivedAt
- Persists Disclosure Receipt

### Repository

- Save Disclosure Receipt
- Find by composite business key
- Duplicate existence check

---

# Quality Gates

- Java 25
- Spring Boot 4.1.x
- Maven
- Azure SQL
- Spring Data JPA
- Java Records for DTOs
- Constructor Injection Only
- Spring Validation
- OpenAPI / Swagger
- JUnit 5
- MockMvc
- Lombok
- No JPA entities exposed via API
- All tests pass using:

```bash
mvn clean test
```

---

# API Flow

```text
Client
   │
POST /api/disclosures/v1/disclosure-receipts
   │
   ▼
DisclosureReceiptController
   │
   ▼
CreateDisclosureReceiptUseCase
   │
   ├── Validate Request
   ├── Validate Work Action
   ├── Check Duplicate Receipt
   ├── Persist Disclosure Receipt
   ▼
Return ApiResponse<CreateDisclosureReceiptResponse>
```