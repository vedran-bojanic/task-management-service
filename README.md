# Task Management System with Users API

This is a Spring Boot RESTful web service for managing tasks.
It provides endpoints to **create**, **read**, **update**, and **delete** tasks.
The API also supports user assignment to tasks and status management.

### Features

- Basic user management (Create)
- Task management (Create, Read, Update, Delete)
- Assign tasks to users
- Filter tasks by user and status
- Error handling with appropriate HTTP status codes

### Technologies Used

- **Spring Boot** - Framework for building the application
- **Spring Web** - For building RESTful APIs
- **Spring Data JPA** - For database access using JPA
- **Spring Security** - For basic security only
- **H2 Database** - In-memory database for development and testing
- **JUnit & Mockito** - For unit testing
- **Flyway** - Flyway updates a database from one version to the next using migrations

### Requirements

- Java 21
- Gradle

## Getting Started

### Configuration

1. Clone the repository and navigate to the project directory.

> git clone https://github.com/vedran-bojanic/task-management-service
>
> cd task-management-service

2. Using **Gradle** build the project:

> ./gradlew clean build

All tests should be green and the application is ready to run!

### Running the Application
You can run the Spring Boot application from the command line using:

> ./gradlew bootRun

Or by starting the app through the **Intellj IDEA**.
No custom configuration needed.

**H2 DB** will automatically start as an in-memory DB.

### H2 DB

Application database is available only when the application is up and running.
H2 console is enabled and can be reached on this link: [H2 DB Console](http://localhost:8080/h2-console)

### OpenAPI - endpoints

When the application is started, swagger is available on this link: [OpenAPI](http://localhost:8080/swagger-ui/index.html)

### Postman collection

Import the Postman collection from:
```
src/main/resources/postman/
└── ASEE_task-management.postman_collection.json
```