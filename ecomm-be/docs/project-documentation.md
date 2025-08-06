# E-commerce Backend Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Project Structure](#project-structure)
3. [Technology Stack](#technology-stack)
4. [Setup and Installation](#setup-and-installation)
5. [Core Components](#core-components)
    - [Models](#models)
    - [Repositories](#repositories)
    - [Services](#services)
    - [Controllers](#controllers)
    - [Exception Handling](#exception-handling)
6. [API Endpoints](#api-endpoints)
7. [Database](#database)
8. [Authentication and Authorization](#authentication-and-authorization)
9. [Common Patterns and Best Practices](#common-patterns-and-best-practices)
10. [Troubleshooting](#troubleshooting)
11. [Change Log](#change-log)

## Project Overview

This project is a Spring Boot-based backend for an e-commerce application. It provides RESTful API endpoints for
managing categories, products, users, orders, and other e-commerce related functionalities.

The application follows a layered architecture pattern with clear separation of concerns:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Manages data access
- **Model Layer**: Defines data structures

## Project Structure

```
src/
  main/
    java/ca/robertgleason/ecommbe/
      EcommBeApplication.java       # Main application entry point
      controller/                   # REST controllers
      excepetions/                  # Global exception handling
      model/                        # Entity definitions
      repository/                   # Data access interfaces
      service/                      # Business logic interfaces and implementations
    resources/
      application.properties        # Application configuration
```

## Technology Stack

- **Java**: Core programming language
- **Spring Boot**: Application framework
- **Spring Data JPA**: Data access
- **Hibernate**: ORM for database interactions
- **Jakarta Validation**: Data validation
- **Lombok**: Reduces boilerplate code
- **SLF4J**: Logging framework

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven
- A suitable IDE (IntelliJ IDEA, Eclipse, VS Code)
- Database (H2 in-memory by default, can be configured for PostgreSQL, MySQL, etc.)

### Steps to Run

1. Clone the repository
2. Configure `application.properties` as needed
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` or execute the main class `EcommBeApplication`

## Core Components

### Models

Models represent the database entities. Currently implemented:

#### Category

Located in: `ca/robertgleason/ecommbe/model/Category.java`

Represents product categories in the e-commerce system.

Key attributes:

- `categoryId`: Primary key using database auto-increment
- `categoryName`: Name of the category (required)

```java

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank
    private String categoryName;
}
```

### Repositories

Repositories handle data access operations.

#### CategoryRepository

Located in: `ca/robertgleason/ecommbe/repository/CategoryRepository.java`

Provides data access methods for Category entities. It extends the Spring Data JPA `JpaRepository` interface, which
provides standard CRUD operations.

### Services

Services contain business logic and bridge controllers with repositories.

#### CategoryService

Located in: `ca/robertgleason/ecommbe/service/CategoryService.java`

Service interface that defines operations for Category management:

- `getAllCategories()`: Retrieves all categories
- `createCategory(Category)`: Creates a new category
- `deleteCategory(Long)`: Deletes a category by ID
- `updateCategory(Long, Category)`: Updates an existing category

#### CategoryServiceImpl

Located in: `ca/robertgleason/ecommbe/service/CategoryServiceImpl.java`

Implementation of the CategoryService interface. Contains business logic for category operations:

- Error handling for non-existent categories
- Logging of operations
- Data validation

### Controllers

Controllers handle HTTP requests and responses.

#### CategoryController

Located in: `ca/robertgleason/ecommbe/controller/CategoryController.java`

Provides RESTful endpoints for Category operations:

- `GET /api/public/categories`: List all categories
- `POST /api/public/categories`: Create a new category
- `DELETE /api/admin/categories/{id}`: Delete a category (admin only)
- `PUT /api/public/categories/{id}`: Update a category

### Exception Handling

#### MyGlobalExceptionHandler

Located in: `ca/robertgleason/ecommbe/excepetions/MyGlobalExceptionHandler.java`

Centralizes exception handling for the application:

- Handles validation exceptions
- Returns appropriate HTTP status codes
- Provides clear error messages to clients

## API Endpoints

### Category API

| Method | Endpoint                    | Description         | Access Level |
|--------|-----------------------------|---------------------|--------------|
| GET    | /api/public/categories      | List all categories | Public       |
| POST   | /api/public/categories      | Create a category   | Public       |
| PUT    | /api/public/categories/{id} | Update a category   | Public       |
| DELETE | /api/admin/categories/{id}  | Delete a category   | Admin        |

## Database

The application is configured to use an in-memory database by default (H2). This can be changed by updating the
`application.properties` file to connect to a different database.

### Sample Database Configuration (PostgreSQL)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## Authentication and Authorization

Currently, the application distinguishes between public and admin endpoints but does not implement actual
authentication. Future versions will integrate Spring Security.

Planned security features:

- JWT-based authentication
- Role-based access control
- Password hashing
- CSRF protection

## Common Patterns and Best Practices

### Service Layer Pattern

- Define interfaces for all services
- Implement business logic in service implementations
- Use constructor injection for dependencies
- Handle exceptions appropriately

### Controller Design

- Keep controllers thin (no business logic)
- Use appropriate HTTP methods
- Return proper HTTP status codes
- Validate input data

### Exception Handling

- Centralize exception handling with @RestControllerAdvice
- Use appropriate exception types
- Provide meaningful error messages

## Troubleshooting

### Common Issues

1. **Database connection issues**
    - Check database credentials in application.properties
    - Ensure database server is running

2. **Validation errors**
    - Ensure request data meets validation requirements
    - Check error response for specific field errors

3. **404 Not Found errors**
    - Verify URL paths
    - Check if IDs exist in the database

## Change Log

### Version 0.1.0 (Initial Version) - August 5, 2023

- Basic project structure
- Category management (CRUD operations)
- Global exception handling
- API documentation
