# E-commerce Backend Documentation

Last Updated: August 14, 2025
Version: 0.0.1-SNAPSHOT

## Table of Contents

1. [Project Overview](#project-overview)
2. [Project Structure](#project-structure)
3. [Technology Stack](#technology-stack)
4. [Setup and Installation](#setup-and-installation)
5. [Core Components](#core-components)
    - [Models](#models)
    - [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
    - [Repositories](#repositories)
    - [Services](#services)
    - [Controllers](#controllers)
    - [Security Components](#security-components)
    - [Exception Handling](#exception-handling)
6. [API Endpoints](#api-endpoints)
    - [Authentication Endpoints](#authentication-endpoints)
    - [Category Management](#category-management)
    - [Product Management](#product-management)
    - [User Management](#user-management)
    - [Address Management](#address-management)
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
      payload/                      # Data Transfer Objects
      config/                       # Application configuration
      utilties/                     # Utility classes
    resources/
      application.properties        # Application configuration
```

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.x**
- **Spring Data JPA**: Data access
- **Spring Security**: Authentication and authorization
- **JWT (JSON Web Tokens)**: Secure authentication mechanism
- **Hibernate**: ORM for database interactions
- **Jakarta Validation**: Data validation
- **Lombok**: Reduces boilerplate code
- **SLF4J**: Logging framework
- **ModelMapper**: Object mapping between DTOs and entities
- **BCrypt**: Password encryption

## Setup and Installation

### Prerequisites

- Java 21 or higher
- Maven
- A suitable IDE (IntelliJ IDEA, Eclipse, VS Code)
- Database (H2 in-memory by default, can be configured for PostgreSQL, MySQL, etc.)

### Steps to Run

1. Clone the repository
2. Configure `application.properties` as needed
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` or execute the main class `EcommBeApplication`

## Core Components

#### Models

- Category: Basic category management
- Product: Associated with Category (many-to-one)
- User: Username, email, password, roles
- Role: Enum-backed roles (ROLE_USER, ROLE_SELLER, ROLE_ADMIN)
- Address: Linked to User

#### User

Located in: `ca/robertgleason/ecommbe/model/User.java`

Represents users in the e-commerce system.

Key attributes:

- `id`: Primary key using database auto-increment
- `username`: Unique username for authentication
- `email`: User's email address (unique)
- `password`: Encrypted password (using BCrypt)
- `roles`: Set of Role entities assigned to the user

#### Role

Located in: `ca/robertgleason/ecommbe/model/Role.java`

Represents role-based access control for users.

Key attributes:

- `id`: Primary key using database auto-increment
- `roleName`: Enum-based role name (ROLE_USER, ROLE_ADMIN, ROLE_SELLER)

#### AppRole

Located in: `ca/robertgleason/ecommbe/model/AppRole.java`

Enum defining the available roles in the system:

- `ROLE_USER`: Standard user with basic permissions
- `ROLE_SELLER`: User with seller privileges
- `ROLE_ADMIN`: Administrator with full system access

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

#### Product

Located in: `ca/robertgleason/ecommbe/model/Product.java`

Represents products in the e-commerce system.

Key attributes:

- `productId`: Primary key using database auto-increment
- `name`: Product name
- `description`: Product description
- `price`: Product price
- `isActive`: Product availability status
- `imageUrl`: URL to product image
- `category`: Reference to the product's category

### Data Transfer Objects (DTOs)

DTOs are used to transfer data between layers of the application, particularly between the service layer and the
controller layer.

#### CategoryDTO

Located in: `ca/robertgleason/ecommbe/payload/CategoryDTO.java`

Used for transferring category data to and from the client.

Key attributes:

- `id`: Category identifier
- `name`: Category name

#### CategoryResponse

Located in: `ca/robertgleason/ecommbe/payload/CategoryResponse.java`

Used for wrapping a list of CategoryDTO objects when returning multiple categories.

Key attributes:

- `content`: List of CategoryDTO objects
- `pageNumber`: Current page number
- `pageSize`: Number of items per page
- `totalElements`: Total number of items
- `totalPages`: Total number of pages
- `lastPage`: Whether this is the last page

#### ProductDTO

Located in: `ca/robertgleason/ecommbe/payload/ProductDTO.java`

Used for transferring product data to and from the client.

Key attributes:

- `id`: Product identifier
- `name`: Product name
- `description`: Product description
- `price`: Product price
- `isActive`: Product availability status
- `imageUrl`: URL to product image
- `categoryId`: ID of the product's category

#### ProductResponse

Located in: `ca/robertgleason/ecommbe/payload/ProductResponse.java`

Used for wrapping a list of ProductDTO objects when returning multiple products.

Key attributes:

- `content`: List of ProductDTO objects
- `pageNumber`: Current page number
- `pageSize`: Number of items per page
- `totalElements`: Total number of items
- `totalPages`: Total number of pages
- `lastPage`: Whether this is the last page

#### UserDTO

Located in: `ca/robertgleason/ecommbe/payload/UserDTO.java`

Used for transferring user data to and from the client.

Key attributes:

- `id`: User identifier
- `username`: Username
- `email`: Email address
- `password`: Password
- `role`: User role (e.g., admin, seller, buyer)

#### AddressDTO

Located in: `ca/robertgleason/ecommbe/payload/AddressDTO.java`

Used for transferring address data to and from the client.

Key attributes:

- `id`: Address identifier
- `street`: Street name and number
- `city`: City
- `state`: State or province
- `zipCode`: ZIP or postal code
- `country`: Country
- `userId`: ID of the user to whom the address belongs

### Repositories

Repositories provide data access functionality using Spring Data JPA.

#### CategoryRepository

Located in: `ca/robertgleason/ecommbe/repository/CategoryRepository.java`

Extends JpaRepository to provide CRUD operations for Category entities.

Custom methods:

- `findByCategoryName`: Finds a category by its name

#### ProductRepository

Located in: `ca/robertgleason/ecommbe/repository/ProductRepository.java`

Extends JpaRepository to provide CRUD operations for Product entities.

Custom methods:

- `findByCategory_CategoryId`: Finds products by their category ID

#### UserRepository

Located in: `ca/robertgleason/ecommbe/repository/UserRepository.java`

Extends JpaRepository to provide CRUD operations for User entities.

Custom methods:

- `findByUsername`: Finds a user by their username
- `findByEmail`: Finds a user by their email

#### AddressRepository

Located in: `ca/robertgleason/ecommbe/repository/AddressRepository.java`

Extends JpaRepository to provide CRUD operations for Address entities.

Custom methods:

- `findByUser_UserId`: Finds addresses by the user's ID

### Services

Services contain the business logic of the application.

#### CategoryService

Located in: `ca/robertgleason/ecommbe/service/CategoryService.java`

Interface defining operations for Category management:

- `getAllCategories(Integer, Integer, String, String)`: Retrieve all categories with pagination
- `createCategory(CategoryDTO)`: Create a new category
- `deleteCategory(Long)`: Delete a category by ID
- `updateCategory(Long, CategoryDTO)`: Update an existing category
- `getCategoryById(Long)`: Get a category by ID

#### CategoryServiceImpl

Located in: `ca/robertgleason/ecommbe/service/CategoryServiceImpl.java`

Implementation of the CategoryService interface with the following features:

- Validation of category existence before operations
- Prevention of duplicate category names
- Comprehensive error handling
- Logging of operations and errors
- Clean transaction management
- Pagination and sorting support

#### ProductService

Located in: `ca/robertgleason/ecommbe/service/ProductService.java`

Interface defining operations for Product management:

- `getAllProducts(Integer, Integer, String, String)`: Retrieve all products with pagination
- `getProductsByCategory(Long, Integer, Integer, String, String)`: Retrieve products by category with pagination
- `createProduct(ProductDTO)`: Create a new product
- `deleteProduct(Long)`: Delete a product by ID
- `updateProduct(Long, ProductDTO)`: Update an existing product
- `getProductById(Long)`: Get a product by ID

#### ProductServiceImpl

Located in: `ca/robertgleason/ecommbe/service/ProductServiceImpl.java`

Implementation of the ProductService interface with similar features to CategoryServiceImpl.

#### FileService

Located in: `ca/robertgleason/ecommbe/service/FileService.java`

Interface for file operations:

- `uploadImage(String, MultipartFile)`: Upload an image file
- `getResource(String, String)`: Retrieve a file resource

#### FileServiceImpl

Located in: `ca/robertgleason/ecommbe/service/FileServiceImpl.java`

Implementation of the FileService interface for handling file uploads and retrievals.

#### UserService

Located in: `ca/robertgleason/ecommbe/service/UserService.java`

Interface defining operations for User management:

- `registerUser(UserDTO)`: Register a new user
- `getUserById(Long)`: Get a user by ID
- `updateUser(Long, UserDTO)`: Update an existing user
- `deleteUser(Long)`: Delete a user by ID
- `getAllUsers(Integer, Integer)`: Retrieve all users with pagination

#### UserServiceImpl

Located in: `ca/robertgleason/ecommbe/service/UserServiceImpl.java`

Implementation of the UserService interface with the following features:

- User registration with role assignment (admin, seller, buyer)
- Comprehensive validation for user fields
- Unique constraint enforcement for usernames and emails
- Error handling and logging
- Pagination support for user retrieval

#### AddressService

Located in: `ca/robertgleason/ecommbe/service/AddressService.java`

Interface defining operations for Address management:

- `createAddress(AddressDTO)`: Create a new address
- `getAddressById(Long)`: Get an address by ID
- `updateAddress(Long, AddressDTO)`: Update an existing address
- `deleteAddress(Long)`: Delete an address by ID
- `getAddressesByUserId(Long, Integer, Integer)`: Retrieve all addresses for a user with pagination

#### AddressServiceImpl

Located in: `ca/robertgleason/ecommbe/service/AddressServiceImpl.java`

Implementation of the AddressService interface with features such as:

- Validation of address existence before operations
- Comprehensive error handling
- Logging of operations and errors
- Clean transaction management
- Pagination support for address retrieval

### Controllers

Controllers handle HTTP requests and delegate to services for business logic.

#### CategoryController

Located in: `ca/robertgleason/ecommbe/controller/CategoryController.java`

Provides REST endpoints for Category management:

- `GET /api/public/categories`: Retrieve all categories with pagination
- `POST /api/public/categories`: Create a new category
- `PUT /api/public/categories/{categoryId}`: Update an existing category
- `DELETE /api/admin/categories/{categoryId}`: Delete a category (admin only)

Note the separation between public and admin endpoints for security purposes.

#### ProductController

Located in: `ca/robertgleason/ecommbe/controller/ProductController.java`

Provides REST endpoints for Product management:

- `GET /api/public/products`: Retrieve all products with pagination
- `GET /api/public/categories/{categoryId}/products`: Get products by category with pagination
- `GET /api/public/products/keyword/{keyword}`: Search products by keyword with pagination
- `POST /api/admin/categories/{categoryId}/product`: Create a new product (admin only)
- `PUT /api/admin/products/{productId}`: Update an existing product (admin only)
- `DELETE /api/admin/products/{productId}`: Delete a product (admin only)
- `PUT /api/products/{productId}/image`: Update product image

#### UserController (Planned)

Note: This controller is planned and not present in the current codebase. The following endpoints are subject to change
and will be finalized upon implementation.

#### AddressController (Planned)

Note: This controller is planned and not present in the current codebase. Functionality and endpoints will be documented
upon implementation.

### Security Components

The application implements a comprehensive JWT-based authentication and authorization system using Spring Security.

#### WebSecurityConfig

Located in: `ca/robertgleason/ecommbe/security/WebSecurityConfig.java`

Central configuration class for Spring Security that:

- Configures stateless session management
- Defines security filter chain
- Disables CSRF for stateless JWT-based API
- Configures JWT token filter
- Establishes authentication entry point for unauthorized requests
- Defines accessible endpoints without authentication
- Configures password encoding with BCrypt
- Initializes default roles and test users

#### JwtUtils

Located in: `ca/robertgleason/ecommbe/security/jwt/JwtUtils.java`

Utility class for JWT token operations:

- Generates JWT tokens based on user authentication
- Creates secure HTTP-only cookies containing JWT tokens
- Validates JWT tokens from requests
- Extracts username from JWT tokens
- Handles token expiration and signing
- Provides clean cookies for user logout

#### AuthTokenFilter

Located in: `ca/robertgleason/ecommbe/security/jwt/AuthTokenFilter.java`

JWT authentication filter that:

- Extracts JWT token from HTTP cookies
- Validates the token using JwtUtils
- Loads user details if token is valid
- Sets the authenticated user in Spring Security context
- Passes the request through the filter chain

#### AuthEntryPointJwt

Located in: `ca/robertgleason/ecommbe/security/jwt/AuthEntryPointJwt.java`

Entry point for handling authentication errors:

- Returns 401 Unauthorized responses for invalid authentication
- Formats error responses in JSON format
- Logs authentication failures

#### UserDetailsServiceImpl

Located in: `ca/robertgleason/ecommbe/security/services/UserDetailsServiceImpl.java`

Implements Spring Security's UserDetailsService interface:

- Loads user data from the database for authentication
- Converts application User entity to Spring Security's UserDetails
- Maps user roles to granted authorities

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

### Authentication Endpoints

| Method | Endpoint          | Description                       | Access |
|--------|-------------------|-----------------------------------|--------|
| POST   | /api/auth/signin  | Sign in a user                    | Public |
| POST   | /api/auth/signup  | Register a new user               | Public |
| POST   | /api/auth/signout | Sign out the authenticated user   | User   |
| GET    | /api/auth/user    | Get the authenticated user's info | User   |

### Category Management

| Method | Endpoint                            | Description                        | Access |
|--------|-------------------------------------|------------------------------------|--------|
| GET    | /api/public/categories              | Get all categories with pagination | Public |
| POST   | /api/public/categories              | Create a new category              | Public |
| PUT    | /api/public/categories/{categoryId} | Update an existing category        | Public |
| DELETE | /api/admin/categories/{categoryId}  | Delete a category                  | Admin  |

### Product Management

| Method | Endpoint                                     | Description                                | Access |
|--------|----------------------------------------------|--------------------------------------------|--------|
| GET    | /api/public/products                         | Get all products with pagination           | Public |
| GET    | /api/public/categories/{categoryId}/products | Get products by category with pagination   | Public |
| GET    | /api/public/products/keyword/{keyword}       | Search products by keyword with pagination | Public |
| POST   | /api/admin/categories/{categoryId}/product   | Create a new product in a category         | Admin  |
| PUT    | /api/admin/products/{productId}              | Update an existing product                 | Admin  |
| DELETE | /api/admin/products/{productId}              | Delete a product                           | Admin  |
| PUT    | /api/products/{productId}/image              | Update product image                       | User   |

### User Management (Planned)

Note: User management endpoints are planned and not currently implemented in this codebase. This section will be
finalized upon implementation.

### Address Management (Planned)

Note: Address management endpoints are planned and not currently implemented in this codebase. This section will be
finalized upon implementation.

## Database

The application uses JPA/Hibernate for ORM, which allows for easy switching between different database providers.

### Entity Relationships

Currently implemented:

- Category (standalone entity)
- Product (references Category in a many-to-one relationship)
- User (with roles and addresses)
- Address (many-to-many relationship with User)

Future implementations will include additional entities and their relationships.

## Authentication and Authorization

The application implements JWT-based authentication with stateless sessions and role-based access control.

- Whitelisted (no auth): `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/h2-console/**`, `/api/test/**`,
  `/images/**`
- All other endpoints require authentication
- Roles: `ROLE_USER`, `ROLE_SELLER`, `ROLE_ADMIN`
- Auth flow: `POST /api/auth/signin` issues an HttpOnly JWT cookie; `POST /api/auth/signout` clears it;
  `GET /api/auth/user` returns the current user

## Common Patterns and Best Practices

The project follows several best practices:

1. **Layered Architecture**: Clear separation between controllers, services, and repositories
2. **Interface-based Design**: Services are defined by interfaces, allowing for multiple implementations
3. **Dependency Injection**: Using Spring's DI for loose coupling between components
4. **Exception Handling**: Global exception handling with custom exceptions
5. **Validation**: Using Jakarta Validation for entity validation
6. **Logging**: Comprehensive logging using SLF4J
7. **Resource Naming**: Consistent REST resource naming conventions
8. **Pagination**: Implemented for all list endpoints to manage large datasets
9. **Data Transfer Objects**: Using DTOs to separate internal and external data representations
10. **Response Wrapping**: Consistent response format with metadata for paginated results

## Troubleshooting

### Common Issues

- **404 Not Found**: Check the endpoint URL and HTTP method
- **400 Bad Request**: Validate request payload against entity requirements
- **500 Internal Server Error**: Check server logs for detailed error information

## Change Log

This section tracks all significant changes to the project using [Semantic Versioning](https://semver.org/) (
MAJOR.MINOR.PATCH).

### Version 0.5.1 (August 14, 2025)

- Documentation refresh: professional formatting, added version header, and updated "Last Updated" date.
- Security docs aligned with code: stateless JWT, CSRF disabled, whitelisted endpoints, seeded users.
- Technology updates reflected: Java 21, Spring Boot 3.5.x.
- Clarified planned modules (User Management, Address Management) and removed unimplemented endpoint tables.

### Version 0.5.0 (August 11, 2025)

- Implemented comprehensive user profile management system
- Added User entity with username, email, and password fields
- Created Role-based authorization system with User, Seller, and Admin roles
- Implemented Address management for user profiles with detailed address fields
- Added many-to-many relationship between Users and Addresses
- Connected Products to Users (sellers) with one-to-many relationship
- Implemented validation for all user profile fields
- Added unique constraints for username and email

### Version 0.4.0 (August 12, 2025)

- Implemented JWT-based authentication system with token management
- Added user registration and login functionality
- Created role-based authorization (USER, SELLER, ADMIN roles)
- Implemented secure password handling with BCrypt encryption
- Added JWT token generation, validation, and cookie-based storage
- Created endpoints for user authentication, registration, and logout
- Implemented user profile information retrieval
- Added automatic initialization of default roles and test users
- Fixed CSRF configuration for authentication endpoints

### Version 0.3.0 (August 7, 2025)

- Added Product entity with full CRUD operations
- Implemented file upload functionality for product images
- Enhanced pagination with sorting capabilities for all list endpoints
- Added comprehensive response wrappers for paginated results
- Implemented many-to-one relationship between Product and Category
- Added utility classes for mapping between entities and DTOs
- Added API endpoints for retrieving products by category
- Improved exception handling for file operations

### Version 0.2.0 (August 9, 2025)

- Added Data Transfer Object (DTO) pattern for Category entity
- Implemented ModelMapper for entity-DTO conversion
- Implemented pagination for category listings
- Added sorting capabilities for category results
- Enhanced responses with appropriate HTTP status codes
- Removed code comments for cleaner codebase
- Updated project documentation with DTO information

### Version 0.1.0 (August 9, 2025)

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
