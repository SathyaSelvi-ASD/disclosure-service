---
name: generate-service-tests
description: Generate comprehensive unit tests for Spring Boot service and UseCase classes using JUnit 5 and Mockito. Use this skill when creating, improving, or reviewing service-level unit tests.
---

# Generate Service Tests

## Purpose

Generate comprehensive unit tests for service and UseCase classes in a Spring Boot application.

The tests must validate business logic, dependency interactions, success scenarios, failure scenarios, validation-related behavior where applicable, and important edge cases.

This skill is reusable for any service or UseCase class in the project. Do not create a separate skill for each API or service unless the project has a materially different testing workflow.

---

# Technology Standards

Follow the project's `AGENTS.md` as the primary source of truth.

Unless overridden by `AGENTS.md`, use:

- Java 25
- Spring Boot 4.1.x
- Maven
- JUnit 5
- Mockito
- AssertJ where already used by the project
- Jakarta APIs where applicable

---

# Step 1: Analyze the Target Class

Before generating tests, inspect the target service or UseCase class.

Identify:

- Class name
- Package
- Public methods
- Method parameters
- Return types
- Business rules
- Conditional logic
- Exception handling
- Repository dependencies
- External client dependencies
- Other service dependencies
- Mappers/converters
- Configuration dependencies
- Transaction boundaries
- Optional/null handling
- Collection handling
- Logging or audit behavior where testable

Do not generate tests based only on the method name.

Read the actual implementation and its dependencies.

---

# Step 2: Inspect Existing Project Conventions

Before creating the test:

1. Read `AGENTS.md`.
2. Inspect existing service/UseCase tests.
3. Check the existing test package structure.
4. Check existing naming conventions.
5. Check whether the project uses:
   - `@ExtendWith(MockitoExtension.class)`
   - `@Mock`
   - `@InjectMocks`
   - AssertJ
   - Mockito `verify`
   - `ArgumentCaptor`
   - parameterized tests
6. Reuse existing conventions where practical.

Do not introduce a new testing framework or library unnecessarily.

---

# Step 3: Choose the Test Type

For service/UseCase unit tests, prefer an isolated Mockito-based test.

Typical structure:

```text
@ExtendWith(MockitoExtension.class)
class WorkActionServiceTest {

    @Mock
    private WorkActionRepository workActionRepository;

    @Mock
    private WorkActionMapper workActionMapper;

    @InjectMocks
    private WorkActionService workActionService;
}
```

Do not use `@SpringBootTest` for a pure service unit test unless the service genuinely requires Spring container behavior that cannot reasonably be tested in isolation.

Do not connect to:

- Real database
- Kafka
- LocalStack
- Azure
- External REST APIs
- Other external infrastructure

during a unit test.

Mock external dependencies.

---

# Step 4: Generate Test Scenarios

For every public method, identify applicable scenarios.

## Happy Path

Test successful execution.

Examples:

- Valid input
- Expected dependency result
- Expected successful response
- Correct repository/client invocation

Example:

```text
@Test
void shouldCreateWorkActionSuccessfully() {
    // Arrange
    // Act
    // Assert
}
```

---

## Empty Result

Where applicable, test:

- Empty `Optional`
- Empty list
- Empty collection
- No matching records

Example:

```text
when(repository.findByCustomerId("C001"))
        .thenReturn(List.of());
```

Verify the expected service behavior.

---

## Not Found

If the service is responsible for handling missing data, test the not-found scenario.

Example:

```text
when(repository.findById("WA001"))
        .thenReturn(Optional.empty());
```

Verify the expected exception or response behavior according to the existing project implementation.

Do not invent a new exception type or error response.

---

## Business Rule Failures

Test each meaningful business rule implemented by the service.

Examples:

- Invalid state transition
- Duplicate record
- Unsupported type
- Missing required business data
- Invalid status
- Invalid combination of fields

Each important branch should have an appropriate test.

---

## Exception Scenarios

Test exceptions from dependencies where the service has explicit handling.

Examples:

```text
when(repository.save(any()))
        .thenThrow(new DataAccessException("Database error") {});
```

Verify that the service:

- Propagates the exception when appropriate
- Translates it when the implementation requires translation
- Returns/throws the expected application exception
- Does not perform unintended operations

Do not assert an exception translation that does not exist in the production code.

---

# Step 5: Verify Dependency Interactions

Tests must verify important interactions with mocked dependencies.

Example:

```text
verify(repository).save(any(WorkActionEntity.class));
```

For negative scenarios:

```text
verify(repository, never()).save(any());
```

Verify:

- Correct method called
- Correct arguments
- Correct number of invocations
- No unintended calls

Use `times(1)` only when it adds meaningful clarity.

Avoid excessive interaction verification for implementation details that are not relevant to the behavior being tested.

---

# Step 6: Verify Arguments

When the exact object passed to a dependency is important, use `ArgumentCaptor`.

Example:

```text
ArgumentCaptor<WorkActionEntity> captor =
        ArgumentCaptor.forClass(WorkActionEntity.class);

verify(repository).save(captor.capture());

WorkActionEntity savedEntity = captor.getValue();

assertThat(savedEntity.getCustomerId())
        .isEqualTo("C001");
```

Use captors when they provide meaningful validation.

Do not use captors unnecessarily when simple Mockito matchers are sufficient.

---

# Step 7: Verify Return Values

Assertions must validate the actual service result.

For example:

```text
assertThat(result).isNotNull();
assertThat(result.getId()).isEqualTo("WA001");
```

For collections:

```text
assertThat(result)
        .hasSize(2)
        .extracting(SearchWorkActionResponse::getId)
        .containsExactly("WA001", "WA002");
```

For `Optional`:

```text
assertThat(result)
        .isPresent()
        .contains(expectedResponse);
```

Use AssertJ if it is already part of the project testing standards.

---

# Step 8: Null and Edge Cases

Test meaningful edge cases based on the implementation.

Consider:

- Null input where the method permits it
- Empty string
- Blank string
- Empty collection
- Boundary values
- Duplicate values
- Missing optional data
- Large collection
- Unexpected dependency response

Do not create meaningless tests for impossible states.

If validation is handled by the controller layer, do not duplicate controller validation tests in the service test unless the service has its own validation/business rule.

---

# Step 9: Transaction and Persistence Behavior

If the service uses transactional behavior, inspect the implementation.

For example:

```text
@Transactional
public ...
```

Do not attempt to test Spring's `@Transactional` framework behavior in a unit test.

Instead, test the business behavior performed within the method.

Integration tests can be used when actual transaction/database behavior needs verification.

---

# Step 10: Test Naming

Use descriptive names that explain the behavior.

Preferred:

```text
shouldCreateWorkActionSuccessfully()

shouldReturnWorkActionsForCustomer()

shouldReturnEmptyListWhenCustomerHasNoWorkActions()

shouldThrowNotFoundExceptionWhenWorkActionDoesNotExist()

shouldPropagateRepositoryException()

shouldNotSaveWorkActionWhenBusinessRuleFails()
```

Avoid vague names:

```text
testCreate()

testSearch()

testMethod()

testService()
```

---

# Step 11: Test Structure

Use Arrange / Act / Assert.

Example:

```text
@Test
void shouldCreateWorkActionSuccessfully() {
    // Arrange
    CreateWorkActionRequest request = ...;
    WorkActionEntity entity = ...;
    CreateWorkActionResponse expectedResponse = ...;

    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toResponse(entity)).thenReturn(expectedResponse);

    // Act
    CreateWorkActionResponse result =
            service.create(request);

    // Assert
    assertThat(result).isEqualTo(expectedResponse);

    verify(mapper).toEntity(request);
    verify(repository).save(entity);
    verify(mapper).toResponse(entity);
}
```

Keep each test focused on one behavior.

---

# Step 12: Mockito Standards

Use Mockito consistently.

Prefer:

```text
when(repository.findById("WA001"))
        .thenReturn(Optional.of(entity));
```

Use:

```text
verify(repository).findById("WA001");
```

For generic arguments:

```text
when(repository.save(any(WorkActionEntity.class)))
        .thenReturn(entity);
```

Avoid unnecessary stubbing.

Do not use `lenient()` unless there is a specific reason.

Avoid mocking the class under test.

---

# Step 13: Exception Assertions

Use JUnit 5 `assertThrows` where appropriate:

```text
var exception = assertThrows(
        WorkActionNotFoundException.class,
        () -> service.findById("WA001")
);

assertThat(exception.getMessage())
        .contains("WA001");
```

If the project uses a custom exception hierarchy, follow the existing implementation.

Do not create or modify production exception classes simply to satisfy a test.

---

# Step 14: Test Coverage Expectations

Aim to cover:

- Every public service/UseCase method
- Happy paths
- Important conditional branches
- Business rules
- Not-found scenarios
- Empty results
- Dependency failures
- Exception handling
- Important edge cases

Focus on meaningful behavioral coverage rather than achieving a high percentage with trivial tests.

---

# Step 15: Do Not Modify Production Code

When generating service tests:

- Do not modify production code just to make tests pass.
- Do not remove business logic.
- Do not weaken validation.
- Do not change exception behavior.
- Do not introduce test-specific production code.
- Do not change API contracts.

If the implementation appears difficult or impossible to test, report the issue instead of silently changing production behavior.

---

# Step 16: Run Tests

After generating tests, run the relevant Maven test.

For a specific test class:

```bash
mvn -Dtest=WorkActionServiceTest test
```

For the complete test suite:

```bash
mvn test
```

If tests fail:

1. Analyze the failure.
2. Determine whether the problem is in the test or production code.
3. Correct the test when the test is wrong.
4. Do not modify production code unless explicitly requested.
5. Re-run the relevant tests.

Never claim tests passed unless they were actually executed.

---

# Step 17: Final Output

After completing the work, provide:

1. Test class created/updated.
2. Service/UseCase methods covered.
3. Test scenarios covered.
4. Important scenarios not covered and why.
5. Maven test command executed.
6. Actual test result.

Example:

```text
Service test generated:

WorkActionServiceTest

Covered:
- create success
- search success
- empty search result
- get by ID success
- get by ID not found
- repository exception
- business rule failure

Test command:
mvn -Dtest=WorkActionServiceTest test

Result:
PASS
```

Do not report `PASS` unless the command was actually executed.