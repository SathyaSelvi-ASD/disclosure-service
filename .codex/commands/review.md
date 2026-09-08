---

## description: Review Disclosure Service code against API specifications, project coding standards, architecture, security, and testing standards.

# Review Disclosure Service

Use the `code-review` skill to perform a complete code review.

## Input

The user will provide the API or code to review.

Examples:

```text
/review Disclosure Receipt Search API
/review Disclosure Receipt Create API
/review DisclosureController
/review src/main/java/.../DisclosureController.java
```

## Instructions

1. Identify the relevant API specification from the project.

2. For example:

```text
DISCLOSURE_RECEIPT_SEARCH_API.md
DISCLOSURE_RECEIPT_CREATE_API.md
WORK_ACTION_CREATE_API.md
```

3. Load and follow the `code-review` skill.

4. Review the implementation against the specification.

5. Inspect related code when required, including:

```text
Controller
DTO
UseCase
Service
Repository
Entity
Exception Handler
Tests
```

6. Check:

* API contract
* Request/response DTOs
* Validation
* Business logic
* Architecture
* Exception handling
* HTTP status codes
* Logging
* Security
* Database/JPA
* Performance
* Code quality
* Unit tests
* API/controller tests
* Negative test cases

7. Do not modify any files unless the user explicitly asks to fix the findings.

8. Do not claim tests pass unless they were actually executed.

## Output

Return the review using the format defined by the `code-review` skill:

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

### Detailed Findings

...

### Specification Compliance

...

### Test Coverage

...

### Positive Observations

...

### Final Recommendation

...
```
