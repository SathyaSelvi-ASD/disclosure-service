# Disclosure Receipt Search API

## API

* **Method:** POST
* **Path:** `/api/disclosures/v1/disclosure-receipts/search`

---

# Request

## Request Type

`DisclosureReceiptSearchRequest`

### Request Body

```json
{
  "workActionId": "WA10001",
  "customerId": "12345",
  "receiptType": "DISCLOSURE",
  "deliveryChannel": "EMAIL",
  "status": "CREATED",
  "referenceNumber": "DISC-REF-10001",
  "fromReceivedAt": "2026-09-01T00:00:00Z",
  "toReceivedAt": "2026-09-08T23:59:59Z"
}
```

> Search criteria are optional unless explicitly marked as mandatory by the implementation. The request should support searching using one or more available criteria.

---

# Response

## Success Response

* **HTTP Status:** `200 OK`
* **Response Type:** `ApiResponse<DisclosureReceiptSearchResponse>`

### Response Structure

```text
ApiResponse
├── status
├── statusCode
├── message
├── data
│   └── DisclosureReceiptSearchResponse
│       └── disclosureReceipts
│           └── DisclosureReceiptResponse
├── warnings
└── errors
```

### Response Body

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "message": "Disclosure receipts retrieved successfully.",
  "data": {
    "disclosureReceipts": [
      {
        "disclosureReceiptId": "DR10001",
        "workActionId": "WA10001",
        "customerId": "12345",
        "receiptType": "DISCLOSURE",
        "deliveryChannel": "EMAIL",
        "recipient": "[REDACTED_EMAIL_ADDRESS_1]",
        "receivedAt": "2026-09-03T10:30:00Z",
        "referenceNumber": "DISC-REF-10001",
        "status": "CREATED"
      }
    ]
  },
  "warnings": [],
  "errors": []
}
```

---

# Business Requirement

Search Disclosure Receipts based on one or more supplied search criteria.

The API retrieves disclosure receipt information for auditing, compliance, customer servicing, and operational purposes.

The search operation must not modify any Disclosure Receipt data.

---

# Validation

## Search Criteria

The following search criteria may be supplied:

* workActionId
* customerId
* receiptType
* deliveryChannel
* status
* referenceNumber
* fromReceivedAt
* toReceivedAt

At least one search criterion should be provided.

## Business Validation

* `fromReceivedAt` cannot be after `toReceivedAt`.
* Date/time range must be valid.
* Supplied enum values must be valid.
* Search criteria must conform to the validation constraints defined on `DisclosureReceiptSearchRequest`.

---

# Business Rules

| Rule   | Description                                                               |
| ------ | ------------------------------------------------------------------------- |
| BR-001 | At least one search criterion must be provided.                           |
| BR-002 | Work Action ID, when supplied, must be used as an exact search criterion. |
| BR-003 | Customer ID, when supplied, must be used as an exact search criterion.    |
| BR-004 | Receipt Type, when supplied, must be a valid Receipt Type.                |
| BR-005 | Delivery Channel, when supplied, must be a valid Delivery Channel.        |
| BR-006 | Status, when supplied, must be a valid Disclosure Receipt Status.         |
| BR-007 | Reference Number, when supplied, must be used as a search criterion.      |
| BR-008 | `fromReceivedAt` cannot be greater than `toReceivedAt`.                   |
| BR-009 | Search must not modify Disclosure Receipt data.                           |
| BR-010 | JPA entities must not be exposed directly through the API.                |

---

# Search Behavior

The search operation should apply only the criteria supplied in the request.

Example:

```text
Request

customerId = 12345
status = CREATED
```

The search should return Disclosure Receipts matching:

```text
customerId = 12345
AND
status = CREATED
```

When multiple criteria are supplied, the criteria should be combined using logical `AND` unless the existing business requirements specify otherwise.

---

# No Results

When no Disclosure Receipts match the supplied criteria, the API should return:

**HTTP 200 OK**

Example:

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "message": "No disclosure receipts found.",
  "data": {
    "disclosureReceipts": []
  },
  "warnings": [],
  "errors": []
}
```

A search with no matching records is not considered an error.

---

# Success Response

HTTP Status

```text
200 OK
```

Example

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "message": "Disclosure receipts retrieved successfully.",
  "data": {
    "disclosureReceipts": [
      {
        "disclosureReceiptId": "DR10001",
        "workActionId": "WA10001",
        "customerId": "12345",
        "receiptType": "DISCLOSURE",
        "deliveryChannel": "EMAIL",
        "recipient": "[REDACTED_EMAIL_ADDRESS_1]",
        "receivedAt": "2026-09-03T10:30:00Z",
        "referenceNumber": "DISC-REF-10001",
        "status": "CREATED"
      }
    ]
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

* Empty search criteria
* Invalid receipt type
* Invalid delivery channel
* Invalid status
* `fromReceivedAt` after `toReceivedAt`
* Invalid date/time format

Example

```json
{
  "type": "https://example.com/problems/validation-error",
  "title": "Validation Failed",
  "status": 400,
  "detail": "Request validation failed.",
  "instance": "/api/disclosures/v1/disclosure-receipts/search"
}
```

---

## Internal Server Error

**HTTP 500 Internal Server Error**

Returned for unexpected system failures.

The existing global exception handling mechanism must be used.

---

# Application Components

Generate

* `DisclosureReceiptSearchUseCase`
* `DisclosureReceiptSearchCommand`
* `DisclosureReceiptSearchResult`

---

# API Components

Generate

* `DisclosureReceiptSearchRequest`
* `DisclosureReceiptSearchResponse`
* `DisclosureReceiptResponse`

Use Java Records for API DTOs where consistent with the project standards.

---

# Domain Components

Reuse existing domain components where applicable.

* `DisclosureReceipt`
* `DisclosureReceiptRepository`
* `ReceiptType`
* `DeliveryChannel`
* `DisclosureReceiptStatus`

Do not create duplicate domain models if these components are already generated for the Disclosure Receipt Create API.

---

# Infrastructure Components

Generate or update as required:

* `DisclosureReceiptJpaEntity`
* `SpringDataDisclosureReceiptRepository`
* `DisclosureReceiptRepositoryAdapter`
* JPA query/specification implementation for search
* Flyway Migration if required

The search implementation must use the existing Azure SQL configuration.

Do not expose `DisclosureReceiptJpaEntity` directly through the API.

---

# Repository

The domain repository must support searching Disclosure Receipts using the available search criteria.

Example operation:

```java
List<DisclosureReceipt> search(
    DisclosureReceiptSearchCommand command);
```

The infrastructure repository may use Spring Data JPA Specifications or another project-standard dynamic query mechanism.

Example search conditions:

```text
workActionId
customerId
receiptType
deliveryChannel
status
referenceNumber
receivedAt >= fromReceivedAt
receivedAt <= toReceivedAt
```

Only supplied criteria should be included in the query.

---

# Controller

Generate

```java
@PostMapping("/disclosure-receipts/search")
public ResponseEntity<ApiResponse<DisclosureReceiptSearchResponse>>
searchDisclosureReceipts(
    @Valid @RequestBody DisclosureReceiptSearchRequest request)
```

The controller must:

1. Accept the search request.
2. Validate the request using `@Valid`.
3. Convert the request into the application command.
4. Delegate processing to `DisclosureReceiptSearchUseCase`.
5. Convert the result into the API response.
6. Return the appropriate HTTP response.

The controller must not contain business or persistence logic.

---

# Controller Example

```java
@PostMapping("/disclosure-receipts/search")
public ResponseEntity<ApiResponse<DisclosureReceiptSearchResponse>> searchDisclosureReceipts(
        @Valid @RequestBody DisclosureReceiptSearchRequest request) {

    log.info("Processing disclosure receipt search request");

    DisclosureReceiptSearchResult result =
            useCase.search(request);

    return ResponseEntity.ok(
            ApiResponse.success(
                    "Disclosure receipts retrieved successfully.",
                    result.toResponse()));
}
```

> Adapt the response construction to the existing `ApiResponse` implementation in the project.

---

# Testing

Generate

* `DisclosureReceiptControllerTest`
* `DisclosureReceiptSearchUseCaseTest`
* `DisclosureReceiptRepositoryTest`

---

# Test Scenarios

## Controller

### 1. Successful Search

Verify:

* POST request is accepted.
* Correct endpoint is used.
* Valid request is accepted.
* Use case is invoked.
* HTTP status is `200 OK`.
* Response contains the matching Disclosure Receipts.

### 2. Empty Search Result

Verify:

* Use case returns an empty result.
* HTTP status is `200 OK`.
* Response contains an empty `disclosureReceipts` collection.

### 3. Empty Search Criteria

Verify:

* Request validation fails.
* HTTP status is `400 Bad Request`.
* Use case is not invoked.

### 4. Invalid Receipt Type

Verify:

* Invalid Receipt Type is rejected.
* HTTP status is `400 Bad Request`.

### 5. Invalid Delivery Channel

Verify:

* Invalid Delivery Channel is rejected.
* HTTP status is `400 Bad Request`.

### 6. Invalid Status

Verify:

* Invalid status is rejected.
* HTTP status is `400 Bad Request`.

### 7. Invalid Date Range

Verify:

* `fromReceivedAt > toReceivedAt`.
* HTTP status is `400 Bad Request`.

### 8. Internal Error

Verify that unexpected exceptions are handled by the existing global exception handler.

---

# Use Case

## Successful Search

Verify:

* Search command is created.
* Supplied search criteria are processed.
* Repository search is invoked.
* Matching receipts are returned.
* Domain objects are converted to the search result.

## Multiple Criteria

Verify that multiple search criteria are combined correctly.

Example:

```text
customerId = 12345
status = CREATED
deliveryChannel = EMAIL
```

Expected behavior:

```text
customerId = 12345
AND status = CREATED
AND deliveryChannel = EMAIL
```

## Date Range

Verify:

* Search by `fromReceivedAt`.
* Search by `toReceivedAt`.
* Search by both date boundaries.

## Empty Result

Verify that an empty repository result produces an empty response collection and does not throw an exception.

---

# Repository

Test the following:

### Search by Work Action

```text
workActionId
```

### Search by Customer

```text
customerId
```

### Search by Receipt Type

```text
receiptType
```

### Search by Delivery Channel

```text
deliveryChannel
```

### Search by Status

```text
status
```

### Search by Reference Number

```text
referenceNumber
```

### Search by Date Range

```text
fromReceivedAt
toReceivedAt
```

### Search by Multiple Criteria

Verify that all supplied criteria are applied using the expected logical combination.

### Empty Result

Verify that the repository returns an empty collection when no records match.

---

# Quality Gates

* Java 25
* Spring Boot 4.1.x
* Maven
* Azure SQL
* Spring Data JPA
* Java Records for DTOs
* Constructor Injection Only
* Spring Validation
* OpenAPI / Swagger
* JUnit 5
* MockMvc
* Lombok
* RFC 9457 Problem Details
* No JPA entities exposed via API
* Existing Disclosure Receipt domain components must be reused
* All tests pass using:

```bash
mvn clean test
```

---

# API Flow

```text
Client
   │
POST /api/disclosures/v1/disclosure-receipts/search
   │
   ▼
DisclosureReceiptController
   │
   ▼
DisclosureReceiptSearchUseCase
   │
   ├── Validate Search Criteria
   ├── Build Search Command
   ├── Execute Receipt Search
   ▼
DisclosureReceiptRepository
   │
   ▼
SpringDataDisclosureReceiptRepository
   │
   ▼
Azure SQL
   │
   ▼
DisclosureReceiptSearchResult
   │
   ▼
DisclosureReceiptSearchResponse
   │
   ▼
ApiResponse<DisclosureReceiptSearchResponse>
```

---

# Implementation Constraints

1. Follow the existing project package structure.
2. Reuse the Disclosure Receipt domain model created for the Create API.
3. Reuse `ReceiptType`, `DeliveryChannel`, and `DisclosureReceiptStatus`.
4. Do not create duplicate entities or domain objects.
5. Use Java Records for request/response DTOs.
6. Use constructor injection only.
7. Keep the controller thin.
8. Business validation belongs in the application/domain layer as appropriate.
9. Persistence logic belongs in the infrastructure layer.
10. Do not expose JPA entities through the REST API.
11. Use the existing RFC 9457 exception-handling mechanism.
12. Follow existing logging and correlation-ID standards.
13. Follow existing OpenAPI/Swagger conventions.
14. Do not modify unrelated APIs.
15. All existing tests must continue to pass.
16. Execute:

```bash
mvn clean test
```

before considering the implementation complete.
