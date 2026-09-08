---

name: code-review
description: Review Disclosure Service Java and Spring Boot code against API specifications, project coding standards, architecture, validation, exception handling, security, logging, database usage, and testing standards. Use this skill when reviewing new or modified Disclosure Service code.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Disclosure Service Code Review

## Purpose

Perform a structured code review for the Disclosure Service.

The review must verify that the implementation:

* Matches the API specification
* Follows the project's architecture
* Follows Java and Spring Boot coding standards
* Implements validation correctly
* Handles exceptions consistently
* Uses appropriate HTTP status codes
* Implements logging correctly
* Avoids security vulnerabilities
* Uses database/repository access correctly
* Has sufficient unit and API test coverage
* Does not introduce unnecessary complexity or duplication

---

# Source of Truth

Before reviewing the implementation, identify the relevant API specification.

Example:

```text
WORK_ACTION_CREATE_API.md
DISCLOSURE_RECEIPT_CREATE_API.md
DISCLOSURE_RECEIPT_SEARCH_API.md
```

The API specification is the primary source of truth for:

* HTTP method
* Endpoint
* Request structure
* Response structure
* Required fields
* Validation rules
* Business requirements
* HTTP status codes
* Error behavior

If the implementation differs from the specification, report it as a finding.

Do not assume that the implementation is correct simply because it compiles.

---

# Expected Disclosure Service Architecture

Verify that the implementation follows the established architecture:

```text
Controller
    ↓
UseCase
    ↓
Service / Business Logic
    ↓
Repository
    ↓
Database
```

Typical package structure:

```text
controller/
dto/
usecase/
service/
repository/
entity/
exception/
config/
```

The exact existing project structure should be preferred over introducing a new structure.

---

# Review Workflow

Perform the review in the following order.

## Step 1 — Understand the API Specification

Read the relevant specification file.

Identify:

* API method
* API path
* Request DTO
* Response DTO
* Required fields
* Validation rules
* Business rules
* Success status
* Error status
* Error response structure

Create an internal checklist before reviewing the implementation.

Example:

```text
API:
POST /api/disclosures/v1/disclosure-receipts

Request:
CreateDisclosureReceiptRequest

Required:
workActionId
customerId
receiptType
deliveryChannel

Success:
200 OK

Response:
ApiResponse<CreateDisclosureReceiptResponse>
```

---

# Step 2 — Controller Review

Review the controller first.

Example:

```java
@PostMapping
public ResponseEntity<ApiResponse> create(
        @Valid @RequestBody CreateDisclosureReceiptRequest request) {

    ApiResponse response = useCase.create(request);

    return ResponseEntity
            .status(HttpStatus.valueOf(response.statusCode()))
            .body(response);
}
```

Check:

* Correct HTTP method
* Correct endpoint
* Correct request DTO
* `@Valid` is present when validation is required
* Correct use case is called
* Controller does not contain business logic
* Correct response type
* Correct HTTP status
* Correct API response wrapper
* Logging follows project standards

Flag business logic such as:

```java
if (request.getCustomerId() == null) {
    ...
}

repository.save(...);
```

inside the controller unless the project explicitly requires it.

---

# Step 3 — Request DTO Review

Check the request DTO against the API specification.

Example:

```java
public record CreateDisclosureReceiptRequest(
        @NotBlank
        String workActionId,

        @NotBlank
        String customerId,

        @NotNull
        ReceiptType receiptType,

        @NotNull
        DeliveryChannel deliveryChannel
) {
}
```

Verify:

* All required fields exist
* Field names match the API contract
* Data types are appropriate
* Required fields have validation
* Validation messages follow project conventions
* Enum fields are handled correctly
* No unnecessary fields are exposed

Report missing validation.

Example:

```text
[HIGH] Missing validation

customerId is required according to the API specification but does not have
@NotBlank validation.

Recommendation:
Add the required validation annotation to the request DTO.
```

---

# Step 4 — Use Case Review

Verify that the controller delegates to the correct use case.

Example:

```java
public interface DisclosureReceiptUseCase {

    ApiResponse create(CreateDisclosureReceiptRequest request);
}
```

Check:

* Use case has a clear responsibility
* Business logic is not duplicated
* Correct DTO is used
* Dependencies are injected correctly
* No unnecessary coupling
* Business rules from the specification are implemented

---

# Step 5 — Business Logic Review

Verify every business requirement in the API specification.

For example:

```text
Business Requirement:
Create a disclosure receipt for an existing work action.
```

Check that the implementation:

1. Validates the work action
2. Validates customer information
3. Applies receipt type rules
4. Applies delivery channel rules
5. Creates the receipt
6. Persists the receipt
7. Returns the expected response

If a business requirement from the specification is missing, report it.

---

# Step 6 — Repository Review

Review repository/database interaction.

Check:

* Correct repository is used
* No unnecessary database calls
* Query methods follow project conventions
* Correct entity is persisted
* Transactions are correctly defined
* No N+1 query problem
* No unnecessary eager loading
* Null handling is correct

Example issue:

```java
for (DisclosureReceipt receipt : receipts) {
    customerRepository.findById(receipt.getCustomerId());
}
```

Report this if it causes unnecessary database calls.

---

# Step 7 — Entity Review

Check:

* Correct JPA annotations
* Correct ID generation
* Correct column mappings
* Appropriate relationship mappings
* Correct nullable configuration
* Correct enum mapping
* Audit fields
* No accidental exposure of entities through API responses

Prefer DTOs for API boundaries where the project architecture requires them.

---

# Step 8 — Exception Handling Review

Verify that errors follow the project's exception-handling pattern.

Check:

* Business exceptions
* Validation exceptions
* Resource-not-found scenarios
* Database exceptions
* Unexpected exceptions
* Global exception handling

Do not recommend returning stack traces to API clients.

Bad:

```java
catch (Exception e) {
    return new ApiResponse(
        500,
        e.getMessage()
    );
}
```

Potential issue:

```text
[HIGH] Internal exception details exposed

The implementation returns e.getMessage() directly to the client.

Recommendation:
Return the project's standard error response and log the original exception
internally.
```

---

# Step 9 — API Response Review

Verify the response against the API specification.

Check:

* Response DTO
* Field names
* Field types
* Required response fields
* HTTP status
* ApiResponse wrapper
* Error response format

Example expected structure:

```text
ApiResponse
└── data
    └── CreateDisclosureReceiptResponse
```

Report any contract mismatch.

---

# Step 10 — Logging Review

Check logging.

Good:

```java
log.info(
    "Creating disclosure receipt for workActionId={}",
    request.workActionId()
);
```

Bad:

```java
log.info("Request: {}", request);
```

if the request can contain sensitive information.

Check:

* Correct log level
* Useful context
* Correlation ID where applicable
* No passwords
* No tokens
* No sensitive customer information
* No excessive logging
* Exception cause preserved

---

# Step 11 — Security Review

Check for:

* SQL injection
* Injection vulnerabilities
* Missing authorization
* Sensitive information exposure
* Hardcoded credentials
* Hardcoded secrets
* Unsafe input handling
* Insecure logging
* Improper exception exposure

Immediately report hardcoded secrets.

Example:

```java
private static final String PASSWORD = "password123";
```

Severity:

```text
BLOCKER
```

---

# Step 12 — Test Review

Review all relevant tests.

Expected test categories:

```text
Controller/API Tests
        ↓
Use Case Tests
        ↓
Service Tests
        ↓
Repository Tests where applicable
```

## Controller Tests

Check for:

* Successful request
* Invalid request
* Missing required fields
* Invalid enum/value
* Correct HTTP status
* Correct response body
* Exception response

Example:

```java
mockMvc.perform(
        post("/api/disclosures/v1/disclosure-receipts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
)
.andExpect(status().isOk());
```

## Use Case Tests

Check:

* Happy path
* Business validation
* Existing resource
* Missing resource
* Repository failure
* Exception scenarios

## Negative Tests

Make sure important failure scenarios are covered.

Examples:

```text
Missing workActionId
Invalid customerId
Invalid receiptType
Invalid deliveryChannel
Work action not found
Customer not found
Database failure
```

Do not claim tests pass unless test execution results are available.

---

# Step 13 — Code Quality Review

Check:

* Naming
* Method size
* Class size
* Complexity
* Duplication
* Null handling
* Optional usage
* Stream usage
* Magic values
* Dead code
* Unused imports
* Unnecessary comments
* Unnecessary abstractions

Avoid reporting subjective style preferences as defects.

---

# Step 14 — Specification Compliance

Perform a final comparison:

```text
API Specification
       ↓
Controller
       ↓
DTO
       ↓
UseCase
       ↓
Business Logic
       ↓
Repository
       ↓
Response
       ↓
Tests
```

Every major requirement should have an implementation path.

---

# Severity Classification

Use the following severity levels.

## BLOCKER

Must be fixed before merge.

Examples:

* Security vulnerability
* Data corruption
* Broken API contract
* Application cannot compile
* Critical business logic failure
* Hardcoded credentials

## HIGH

Should be fixed before merge.

Examples:

* Missing required validation
* Incorrect HTTP status
* Missing business requirement
* Incorrect exception handling
* Missing authorization
* Significant database/performance issue
* Important missing test coverage

## MEDIUM

Should be addressed.

Examples:

* Maintainability issue
* Moderate duplication
* Poor error handling
* Missing edge-case test
* Unnecessary database call

## LOW

Optional improvement.

Examples:

* Naming improvement
* Readability improvement
* Minor refactoring
* Small code-quality improvement

---

# Review Output

Always provide the review in this format:

```text
## Code Review Summary

Overall Status:
APPROVE / CHANGES REQUESTED / BLOCK

API Reviewed:
<API name>

Specification:
<specification file>

### Findings

| Severity | File | Line | Issue | Recommendation |
|----------|------|------|-------|----------------|
| BLOCKER | ... | ... | ... | ... |
| HIGH | ... | ... | ... | ... |
| MEDIUM | ... | ... | ... | ... |
| LOW | ... | ... | ... | ... |

### Detailed Findings

#### [HIGH] Missing validation

File:
DisclosureReceiptController.java

Line:
25

Issue:
The request object is accepted without @Valid although the API specification
requires validation.

Recommendation:
Add @Valid to the request parameter and validation annotations to the DTO.

---

### Specification Compliance

| Requirement | Status | Details |
|------------|--------|---------|
| API endpoint | PASS | Matches specification |
| Request DTO | PASS | Matches specification |
| Validation | FAIL | customerId validation missing |
| Business rules | PASS | Implemented |
| Response | PASS | Matches specification |
| Exception handling | PASS | Uses global handler |
| Tests | PARTIAL | Negative scenario missing |

---

### Test Coverage

Controller:
PASS / PARTIAL / MISSING

Use Case:
PASS / PARTIAL / MISSING

Service:
PASS / PARTIAL / MISSING

Repository:
PASS / PARTIAL / MISSING

Missing test scenarios:

- ...
- ...

---

### Positive Observations

- ...
- ...
- ...

---

### Final Recommendation

APPROVE

or

CHANGES REQUESTED

or

BLOCK
```

---

# Review Rules

1. Always check the relevant API specification first.
2. Treat the API specification as the contract.
3. Follow existing Disclosure Service architecture.
4. Do not introduce unnecessary architectural changes.
5. Report only technically meaningful findings.
6. Provide file and line numbers whenever available.
7. Explain why the issue matters.
8. Provide a concrete recommendation.
9. Separate mandatory fixes from optional improvements.
10. Do not modify code unless explicitly requested.
11. Do not claim tests pass unless they were actually executed.
12. Check related classes when necessary.
13. Check both positive and negative test cases.
14. Prioritize security, correctness, API compatibility, and business requirements.
15. If no issues are found, explicitly state that no significant issues were identified.
