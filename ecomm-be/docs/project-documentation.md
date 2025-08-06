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
- `categoryName`: Name of the category (required, minimum 5 characters)

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
    @Size(min = 5, message = "Category name must be at least 5 characters long")
    private String categoryName;
}
```

### Repositories

Repositories provide data access functionality using Spring Data JPA.

#### CategoryRepository

Located in: `ca/robertgleason/ecommbe/repository/CategoryRepository.java`

Extends JpaRepository to provide CRUD operations for Category entities.

Custom methods:

- `findByCategoryName`: Finds a category by its name

### Services

Services contain the business logic of the application.

#### CategoryService

Located in: `ca/robertgleason/ecommbe/service/CategoryService.java`

Interface defining operations for Category management:

- `getAllCategories()`: Retrieve all categories
- `createCategory(Category)`: Create a new category
- `deleteCategory(Long)`: Delete a category by ID
- `updateCategory(Long, Category)`: Update an existing category

#### CategoryServiceImpl

Located in: `ca/robertgleason/ecommbe/service/CategoryServiceImpl.java`

Implementation of the CategoryService interface with the following features:

- Validation of category existence before operations
- Prevention of duplicate category names
- Comprehensive error handling
- Logging of operations and errors
- Clean transaction management

### Controllers

Controllers handle HTTP requests and delegate to services for business logic.

#### CategoryController

Located in: `ca/robertgleason/ecommbe/controller/CategoryController.java`

Provides REST endpoints for Category management:

- `GET /api/public/categories`: Retrieve all categories
- `POST /api/public/categories`: Create a new category
- `DELETE /api/admin/categories/{categoryId}`: Delete a category (admin only)
- `PUT /api/public/categories/{categoryId}`: Update an existing category

Note the separation between public and admin endpoints for security purposes.

### Exception Handling

The application implements a global exception handling strategy to provide consistent error responses.

#### ResourceNotFoundException

Located in: `ca/robertgleason/ecommbe/excepetions/ResourceNotFoundException.java`

Custom exception for when a requested resource cannot be found, providing details about the resource type, field, and
value.

#### APIException

Located in: `ca/robertgleason/ecommbe/excepetions/APIException.java`

General purpose exception for API-related errors such as validation failures or business rule violations.

#### MyGlobalExceptionHandler

Located in: `ca/robertgleason/ecommbe/excepetions/MyGlobalExceptionHandler.java`

Global exception handler that catches exceptions and returns appropriate HTTP responses with meaningful error messages.

## API Endpoints

### Category Management

| Method | Endpoint                            | Description                 | Access |
|--------|-------------------------------------|-----------------------------|--------|
| GET    | /api/public/categories              | Get all categories          | Public |
| POST   | /api/public/categories              | Create a new category       | Public |
| PUT    | /api/public/categories/{categoryId} | Update an existing category | Public |
| DELETE | /api/admin/categories/{categoryId}  | Delete a category           | Admin  |

## Database

The application uses JPA/Hibernate for ORM, which allows for easy switching between different database providers.

### Entity Relationships

Currently implemented:

- Category (standalone entity)

Future implementations will include additional entities and their relationships.

## Authentication and Authorization

The API has endpoints with different access levels:

- Public endpoints: Accessible to all users
- Admin endpoints: Restricted to administrators

*Note: Authentication and authorization mechanisms are not yet implemented but are planned for future development.*

## Common Patterns and Best Practices

The project follows several best practices:

1. **Layered Architecture**: Clear separation between controllers, services, and repositories
2. **Interface-based Design**: Services are defined by interfaces, allowing for multiple implementations
3. **Dependency Injection**: Using Spring's DI for loose coupling between components
4. **Exception Handling**: Global exception handling with custom exceptions
5. **Validation**: Using Jakarta Validation for entity validation
6. **Logging**: Comprehensive logging using SLF4J
7. **Resource Naming**: Consistent REST resource naming conventions

## Troubleshooting

### Common Issues

- **404 Not Found**: Check the endpoint URL and HTTP method
- **400 Bad Request**: Validate request payload against entity requirements
- **500 Internal Server Error**: Check server logs for detailed error information

## Change Log

This section tracks all significant changes to the project using [Semantic Versioning](https://semver.org/) (
MAJOR.MINOR.PATCH).

### Version 0.1.0 (August 5, 2025)

- Initial project setup
- Implemented Category management (CRUD operations)
- Added global exception handling
- Implemented validation for entity fields
- Added basic API documentation

### Version History Format

Entries in the change log follow this format:

- **MAJOR version**: Incompatible API changes
- **MINOR version**: Added functionality in a backward compatible manner
- **PATCH version**: Backward compatible bug fixes

Each version entry includes:

- Version number and release date
- List of added features
- List of changed features
- List of deprecated features
- List of removed features
- List of fixed issues
