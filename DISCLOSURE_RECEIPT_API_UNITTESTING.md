# Generate Test Cases for Create Disclosure Receipt API

## Objective

Generate comprehensive test cases for the following endpoint:

```
POST /api/disclosures/v1/disclosure-receipts
```

Generate test cases at two levels:

1. API (Controller) Tests
2. Service (Use Case) Tests

Use:

- Spring Boot 4.1.x
- Java 25
- JUnit 5
- Mockito
- MockMvc (API Tests)
- AssertJ
- Constructor Injection
- AAA (Arrange-Act-Assert) Pattern

---

# API Test Cases (DisclosureReceiptControllerTest)

## Positive Test Cases

### TC_API_001
**Scenario**
Create disclosure receipt successfully with valid request.

**Expected**
- HTTP 200 OK
- Response contains
    - disclosureReceiptId
    - workActionId
    - customerId
    - status = CREATED

---

## Validation Test Cases

### TC_API_002
Missing workActionId

Expected

- HTTP 400
- Validation error
- ERR-VALIDATION-WORKACTIONID

---

### TC_API_003
Missing customerId

Expected

HTTP 400

---

### TC_API_004
Missing receiptType

Expected

HTTP 400

---

### TC_API_005
Missing deliveryChannel

Expected

HTTP 400

---

### TC_API_006
Missing recipient

Expected

HTTP 400

---

### TC_API_007
Missing receivedAt

Expected

HTTP 400

---

### TC_API_008
Invalid email format

Example

```
recipient = abc@
```

Expected

HTTP 400

---

### TC_API_009
Future receivedAt date

Expected

HTTP 400

---

## Business Exception Tests

### TC_API_010
Duplicate receipt

Expected

HTTP 409

---

### TC_API_011
Work Action not found

Expected

HTTP 404

---

## JSON Validation

### TC_API_012
Malformed JSON

Expected

HTTP 400

---

### TC_API_013
Unsupported Media Type

Content-Type = text/plain

Expected

HTTP 415

---

### TC_API_014
Accept header unsupported

Expected

HTTP 406

---

## Service Invocation

Verify

- createDisclosureReceipt() invoked exactly once

---

# Service Test Cases (CreateDisclosureReceiptUseCaseTest)

Mock

- DisclosureReceiptRepository
- WorkActionRepository (if applicable)

---

## Positive Tests

### TC_SERVICE_001

Given

Valid command

When

execute()

Then

- entity saved
- status CREATED returned

---

## Validation Tests

### TC_SERVICE_002

Duplicate receipt exists

Expected

DuplicateReceiptException

---

### TC_SERVICE_003

Work Action does not exist

Expected

WorkActionNotFoundException

---

### TC_SERVICE_004

Future receivedAt

Expected

ValidationException

---

### TC_SERVICE_005

Invalid email

Expected

ValidationException

---

## Repository Interaction

### TC_SERVICE_006

Verify

repository.save()

called once

---

### TC_SERVICE_007

Verify

repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber()

called once

---

## Entity Mapping

### TC_SERVICE_008

Verify all request fields mapped correctly to entity

- workActionId
- customerId
- receiptType
- deliveryChannel
- recipient
- receivedAt
- referenceNumber
- notes

---

## Response Mapping

### TC_SERVICE_009

Verify response contains

- disclosureReceiptId
- workActionId
- customerId
- CREATED

---

# Edge Cases

- Very long notes
- Empty string recipient
- Null referenceNumber
- Maximum allowed referenceNumber length
- Duplicate request submitted twice
- Email with uppercase letters
- Email containing '+' sign
- Unicode characters in notes

---

# Naming Convention

Controller

```
DisclosureReceiptControllerTest
```

Service

```
CreateDisclosureReceiptUseCaseTest
```

Test Method Naming

```
shouldCreateDisclosureReceiptSuccessfully()

shouldReturn400WhenWorkActionIdMissing()

shouldReturn400WhenEmailInvalid()

shouldThrowDuplicateReceiptException()

shouldThrowWorkActionNotFoundException()

shouldRejectFutureReceivedDate()

shouldSaveDisclosureReceipt()

shouldMapEntityCorrectly()
```

---

# Coverage Goal

Generate tests covering:

- Happy path
- Validation
- Business rules
- Exception handling
- Repository interaction
- DTO mapping
- Response validation

Target test coverage: **90%+**