---
name: generate-controller-tests
description: Generate comprehensive unit tests for Spring Boot REST controllers using JUnit 5, Mockito, MockMvc, and Spring Boot MVC test support. Use this skill when creating or improving controller-level API tests.
---

# Generate Controller Tests

## Purpose

Generate maintainable and comprehensive controller-level unit tests for Spring Boot REST APIs.

The controller tests must validate HTTP/API behavior while isolating the controller from application services, databases, repositories, and external systems.

This skill is reusable across all REST controllers in the project.

---

## Technology Context

The project uses:

- Java 25
- Spring Boot 4.1.x
- JUnit 5
- Mockito
- MockMvc
- Jackson ObjectMapper
- Jakarta Validation
- Maven

Always inspect the project's `pom.xml` before generating tests.

Use the testing APIs and dependencies actually configured in the project.

Do not introduce new dependencies unless explicitly requested.

---

## When to Use

Use this skill when the user asks to:

- Generate controller unit tests
- Generate API tests for a controller
- Increase controller test coverage
- Add missing controller tests
- Create tests for a new REST endpoint
- Update controller tests after a controller change

---

## Scope

This skill applies only to controller/API behavior.

Test:

- HTTP method
- Request URL
- Path variables
- Query parameters
- Request body
- Request validation
- HTTP status
- Response body
- Response headers
- UseCase/service invocation
- Exception handling
- Authorization when applicable

Do not test:

- Business logic
- Repository implementation
- Database operations
- External API implementation
- Kafka implementation
- Infrastructure components

Those should be tested separately.

---

# Project Architecture

The project follows a controller-to-use-case architecture.

Typical flow:

```text
HTTP Request
     |
     v
Controller
     |
     v
UseCase
     |
     v
Repository / External Service


