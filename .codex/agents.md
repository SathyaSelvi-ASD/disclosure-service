# AGENTS.md

## Project

Backend API written in FastAPI.

## Development

Install dependencies

pip install -r requirements.txt

Run server

uvicorn app.main:app --reload

Run tests

pytest

## Coding Rules

- Use type hints.
- Use Black formatting.
- Use Ruff for linting.
- Add docstrings for public functions.
- Prefer async endpoints.

## Database

Use SQLAlchemy ORM.
Never write raw SQL unless necessary.

## Before finishing

- Run tests.
- Run lint.
- Update documentation if APIs changed.

# Project Testing Standards

## Technology Stack

- Java: 25
- Spring Boot: 4.1.x
- Build Tool: Maven
- Testing Framework: JUnit 5
- Mocking Framework: Mockito
- Web Testing: Spring MVC Test / MockMvc
- JSON Serialization: Jackson
- Validation: Jakarta Bean Validation
- Assertions: AssertJ where available

---

# Testing Principles

Follow the testing pyramid:

1. Unit tests
2. Web/controller slice tests
3. Integration tests
4. End-to-end tests

Prefer unit and slice tests wherever possible.

Do not use integration testing when a unit or slice test is sufficient.

---

# Test Package Structure

Keep test classes in the same package structure as production classes.

Example:

src/main/java/
└── com/volvo/workaction/
├── api/
│   └── WorkActionController.java
└── application/
├── CreateWorkActionUseCase.java
├── SearchWorkActionUseCase.java
└── GetWorkActionUseCase.java

src/test/java/
└── com/volvo/workaction/
├── api/
│   └── WorkActionControllerTest.java
└── application/
├── CreateWorkActionUseCaseTest.java
├── SearchWorkActionUseCaseTest.java
└── GetWorkActionUseCaseTest.java