# E-commerce Backend API

A robust and scalable RESTful API for e-commerce applications built with Spring Boot. This backend provides
comprehensive functionality for managing products, categories, and more with a clean architecture and best practices
implementation.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## 🌟 Features

- **Category Management**: CRUD operations for product categories
- **Product Management**: Complete product lifecycle management
- **Image Upload**: File handling for product images
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination
- **Exception Handling**: Comprehensive global exception handling
- **Data Validation**: Robust input validation
- **Clean Architecture**: Separation of concerns with controller, service, and repository layers
- **DTO Pattern**: Data Transfer Objects for clean API contracts
- **RESTful Design**: Follows REST principles with appropriate HTTP methods and status codes

## 🛠️ Technology Stack

- **Java 17**: Core programming language
- **Spring Boot**: Application framework
- **Spring Data JPA**: Data persistence
- **Hibernate**: ORM for database interactions
- **Jakarta Validation**: Input validation
- **Lombok**: Reduces boilerplate code
- **ModelMapper**: Object mapping between DTOs and entities
- **SLF4J**: Logging framework
- **H2 Database**: In-memory database (configurable for production databases)

## 📋 Project Structure

```
src/main/java/ca/robertgleason/ecommbe/
├── config/               # Application configuration
├── controller/           # REST controllers
├── exceptions/           # Global exception handling
├── model/                # Entity definitions
├── payload/              # Data Transfer Objects
├── repository/           # Data access interfaces
├── service/              # Business logic
└── utilities/            # Helper classes
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

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

## 📝 API Documentation

### Categories

| Method | Endpoint                    | Description                    |
|--------|-----------------------------|--------------------------------|
| GET    | /api/public/categories      | Get all categories (paginated) |
| GET    | /api/public/categories/{id} | Get a specific category        |
| POST   | /api/public/categories      | Create a new category          |
| PUT    | /api/public/categories/{id} | Update an existing category    |
| DELETE | /api/admin/categories/{id}  | Delete a category (admin only) |

### Products

| Method | Endpoint                                   | Description                   |
|--------|--------------------------------------------|-------------------------------|
| GET    | /api/public/products                       | Get all products (paginated)  |
| GET    | /api/public/products/{id}                  | Get a specific product        |
| GET    | /api/public/products/category/{categoryId} | Get products by category      |
| POST   | /api/public/products                       | Create a new product          |
| PUT    | /api/public/products/{id}                  | Update an existing product    |
| DELETE | /api/admin/products/{id}                   | Delete a product (admin only) |
| POST   | /api/public/products/image/{productId}     | Upload product image          |
| GET    | /api/public/products/image/{productId}     | Get product image             |

### Pagination and Sorting

All collection endpoints support pagination and sorting with the following query parameters:

- `pageNumber`: Zero-based page index (default: 0)
- `pageSize`: Page size (default: 10)
- `sortBy`: Field to sort by (default varies by endpoint)
- `sortDir`: Sort direction - 'asc' or 'desc' (default: 'asc')

Example:

```
GET /api/public/products?pageNumber=0&pageSize=10&sortBy=price&sortDir=desc
```

## 🔒 Security

The API has two access levels:

- Public endpoints (`/api/public/**`): Accessible to all users
- Admin endpoints (`/api/admin/**`): Restricted to administrators

*Note: Authentication and authorization are planned for future releases.*

## 🧪 Testing

Run the tests with Maven:

```bash
mvn test
```

## 📚 Documentation

For more detailed documentation on the project architecture, design patterns, and best practices, see:

- [Project Documentation](docs/project-documentation.md)
- [Spring Boot Best Practices](docs/spring-boot-best-practices.md)

## 🛣️ Roadmap

- User authentication and authorization
- Order management
- Payment processing integration
- Review and rating system
- Search functionality with filters
- Performance optimizations

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📬 Contact

Robert Gleason - [robgleasonjobs122@gmail.com](mailto:your.email@example.com)

Project Link: [https://github.com/yourusername/ecomm-be](https://github.com/yourusername/ecomm-be)

---

⭐️ From [epiq122](https://github.com/yourusername)
