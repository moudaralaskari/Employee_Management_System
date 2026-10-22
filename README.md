# Employee Management System

A RESTful backend application built with Java and Spring Boot for managing employees and departments, with secure
authentication and role-based access control.

## Features

- Employee and department management
- JWT-based authentication
- Role-based authorization using Spring Security
- Password reset via email
- Request validation
- Centralized exception handling
- MySQL database integration using Spring Data JPA and Hibernate
- Unit and integration testing

## Tech Stack

| Technology                  | Purpose                             |
|-----------------------------|-------------------------------------|
| Java 17                     | Programming language                |
| Spring Boot 4.1.1           | Backend framework                   |
| Spring Security             | Authentication and authorization    |
| Spring Data JPA / Hibernate | Data persistence and ORM            |
| MySQL                       | Relational database                 |
| JJWT 0.12.6                 | JWT implementation                  |
| Spring Boot Mail            | Email functionality                 |
| Maven                       | Build and dependency management     |
| JUnit / Mockito             | Testing                             |
| Testcontainers              | Integration testing with containers |
| REST Assured                | HTTP API testing                    |

## Prerequisites

- JDK 17 or a compatible JDK
- MySQL
- Docker, for integration tests that use Testcontainers

## Configuration

Before running the application, make sure your local MySQL server is running and the database connection URL in
`application.properties` matches your setup.

The current development configuration contains locally defined database credentials and a JWT signing secret. These
values are intended for local development only and must not be reused in production.

For email functionality, configure the following environment variables:

| Variable             | Purpose                |
|----------------------|------------------------|
| `GMAIL_APP_USERNAME` | Email account username |
| `GMAIL_APP_PASSWORD` | Email app password     |

**Security:** Never commit real passwords, JWT secrets, or other sensitive credentials to a public repository. Use
environment variables or a secure secrets manager for production deployments.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/moudaralaskari/Employee_Management_System.git
cd Employee_Management_System
```

### 2. Configure the application

1. Start your local MySQL server.
2. Verify that the database connection URL in `src/main/resources/application.properties` matches your local setup.
3. Configure `GMAIL_APP_USERNAME` and `GMAIL_APP_PASSWORD` if you want to use email functionality.
4. Do not use the local development credentials or JWT secret in production.

### 3. Run the application

Using the Maven Wrapper on macOS or Linux:

```bash
./mvnw spring-boot:run
```

### 4. Run the tests

```bash
./mvnw test
```

Integration tests that use Testcontainers require Docker to be available.

## API Testing

Use Postman or another HTTP client to test the REST endpoints. Some endpoints require a valid JWT and appropriate user
permissions.

## Project Status

The project includes unit and integration tests covering application functionality and API behavior.

## Author

**Moudar Alaskari**

[GitHub Profile](https://github.com/moudaralaskari)
