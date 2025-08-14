# E-commerce Backend API

A robust and scalable RESTful API for e-commerce applications built with Spring Boot. This backend provides
comprehensive functionality for managing products and categories, with JWT-based authentication, clean architecture,
and production-ready best practices.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

Last Updated: August 14, 2025

## 🌟 Features

- **JWT Authentication**: Secure, stateless auth using HttpOnly cookies
- **Role-Based Access**: Admin endpoints protected; public endpoints for reads
- **Category Management**: Create, update, list, and delete categories
- **Product Management**: Create, update, list, and search products
- **Image Upload**: File handling for product images
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination
- **Exception Handling**: Comprehensive global exception handling
- **Validation**: Robust input validation
- **Clean Architecture**: Controller, Service, Repository separation with DTOs

## 🛠️ Technology Stack

- **Java 21**
- **Spring Boot 3.5.x**
- **Spring Security (JWT)**
- **Spring Data JPA / Hibernate**
- **Jakarta Validation**
- **Lombok**
- **ModelMapper**
- **SLF4J**
- **H2 Database** (in-memory by default)

## 📋 Project Structure

```
src/main/java/ca/robertgleason/ecommbe/
├── config/               # Application configuration
├── controller/           # REST controllers
├── excepetions/          # Global exception handling
├── model/                # Entity definitions
├── payload/              # Data Transfer Objects
├── repository/           # Data access interfaces
├── security/             # JWT & security classes
├── service/              # Business logic
└── utilties/             # Helper classes
```

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ecomm-be.git
   cd ecomm-be
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. The API will be available at `http://localhost:8080`

## ⚙️ Configuration

Key application.properties (defaults provided):

```
# H2 & JPA
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:test
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Images
project.image=images/

# JWT
spring.app.jwtSecret=...your-secret...
spring.app.jwtExpirationMs=36000000
spring.app.jwtCookieName=springbootBE

# Logging (optional)
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

- H2 console: http://localhost:8080/h2-console
- Static images served under `/images/**`

## 🔒 Security

- Stateless JWT security via HttpOnly cookies
- Whitelisted (no auth required): `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/h2-console/**`, `/api/test/**`, `/images/**`
- All other endpoints require authentication
- Seeded users (for local testing):
  - admin / adminPass (ROLE_USER, ROLE_SELLER, ROLE_ADMIN)
  - seller1 / password2 (ROLE_SELLER)
  - user1 / password1 (ROLE_USER)

Authentication endpoints:

| Method | Endpoint          | Description                       |
|--------|-------------------|-----------------------------------|
| POST   | /api/auth/signin  | Authenticate; sets JWT cookie     |
| POST   | /api/auth/signup  | Register a new user               |
| POST   | /api/auth/signout | Clear JWT cookie (logout)         |
| GET    | /api/auth/user    | Get current user info (requires auth) |

Note: Path segments like `/public` denote read-oriented resources but still require authentication unless explicitly whitelisted.

## 📝 API Overview

### Categories

| Method | Endpoint                             | Description                             |
|--------|--------------------------------------|-----------------------------------------|
| GET    | /api/public/categories               | Get all categories (paginated)          |
| POST   | /api/public/categories               | Create a new category                    |
| PUT    | /api/public/categories/{categoryId}  | Update an existing category              |
| DELETE | /api/admin/categories/{categoryId}   | Delete a category (admin only)           |

### Products

| Method | Endpoint                                            | Description                                   |
|--------|-----------------------------------------------------|-----------------------------------------------|
| GET    | /api/public/products                                | Get all products (paginated)                  |
| GET    | /api/public/categories/{categoryId}/products        | Get products by category (paginated)          |
| GET    | /api/public/products/keyword/{keyword}              | Search products by keyword (paginated)        |
| POST   | /api/admin/categories/{categoryId}/product          | Create a new product in a category (admin)    |
| PUT    | /api/admin/products/{productId}                     | Update an existing product (admin)            |
| DELETE | /api/admin/products/{productId}                     | Delete a product (admin)                      |
| PUT    | /api/products/{productId}/image                     | Update product image                           |

### Pagination and Sorting

All collection endpoints support pagination/sorting parameters:
- `pageNumber` (default: 0)
- `pageSize` (default: 10)
- `sortBy` (endpoint-specific default)
- `sortOrder`: `asc` or `desc` (default: `asc`)

Example:
```
GET /api/public/products?pageNumber=0&pageSize=10&sortBy=price&sortOrder=desc
```

## 🧪 Testing

```bash
mvn test
```

## 📚 Further Documentation

- Project Documentation: docs/project-documentation.md
- Spring Boot Best Practices: docs/spring-boot-best-practices.md

## 🗓️ Changelog

- 2025-08-14: Upgraded docs and configuration; verified JWT via HttpOnly cookies and stateless security; confirmed seeded users and whitelist; aligned README and project documentation; updated versions (Java 21, Spring Boot 3.5.4).

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📬 Contact

Robert Gleason - robgleasonjobs122@gmail.com

Project Link: https://github.com/yourusername/ecomm-be
