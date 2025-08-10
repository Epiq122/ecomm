# Spring Boot Best Practices

*Last Updated: August 9, 2025*

This document outlines best practices for developing Spring Boot applications, based on experience from this e-commerce
project. These guidelines will help maintain consistency, improve code quality, and facilitate future development.

## Table of Contents

1. [Project Structure](#project-structure)
2. [API Design](#api-design)
3. [Data Layer](#data-layer)
4. [Service Layer](#service-layer)
5. [Exception Handling](#exception-handling)
6. [Security](#security)
7. [Configuration](#configuration)
8. [Testing](#testing)
9. [Performance](#performance)
10. [Deployment](#deployment)

## Project Structure

### Package Organization

- **Use a domain-based package structure** rather than a technical one:
  ```
  com.example.project/
    └── domain/
        ├── product/
        │   ├── Product.java
        │   ├── ProductRepository.java
        │   ├── ProductService.java
        │   └── ProductController.java
        └── order/
            ├── Order.java
            └── ...
  ```

- **For larger applications**, consider a modular approach:
  ```
  com.example.project/
    ├── common/
    │   ├── config/
    │   ├── exception/
    │   └── util/
    ├── module1/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   └── model/
    └── module2/
        └── ...
  ```

### Code Organization

- **Follow the separation of concerns** principle with clear layers:
    - **Controllers**: Handle HTTP requests/responses
    - **Services**: Implement business logic
    - **Repositories**: Manage data access
    - **Models**: Define data structures
    - **DTOs**: Transfer data between layers

- **Use interfaces** for services to allow multiple implementations and facilitate testing

## API Design

### RESTful Principles

- **Use proper HTTP methods**:
    - `GET`: Retrieve resources
    - `POST`: Create resources
    - `PUT`: Update resources (full update)
    - `PATCH`: Partial update of resources
    - `DELETE`: Remove resources

- **Use appropriate HTTP status codes**:
    - `200 OK`: Successful request
    - `201 Created`: Resource created
    - `204 No Content`: Successful request with no response body
    - `400 Bad Request`: Invalid request
    - `401 Unauthorized`: Authentication required
    - `403 Forbidden`: Authenticated but not authorized
    - `404 Not Found`: Resource not found
    - `500 Internal Server Error`: Server-side error

### URL Design

- **Use nouns, not verbs** for resource endpoints:
    - Good: `/api/products`
    - Avoid: `/api/getProducts`

- **Use plural nouns** for collections:
    - Good: `/api/categories`
    - Avoid: `/api/category`

- **Use path parameters for specific resources**:
    - `/api/products/{productId}`

- **Use query parameters for filtering, sorting, and pagination**:
    - `/api/products?category=electronics&sort=price&page=0&size=10`

### Response Structure

- **Use consistent response structures** across all endpoints:
  ```json
  {
    "content": [...],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "lastPage": false
  }
  ```

- **Include error details in error responses**:
  ```json
  {
    "timestamp": "2025-08-09T10:15:30",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid product data",
    "path": "/api/products"
  }
  ```

### Versioning

- **Consider API versioning** from the start:
    - URL-based: `/api/v1/products`
    - Header-based: `Accept: application/vnd.company.app-v1+json`

## Data Layer

### Entity Design

- **Keep entities focused on the database model**:
    - Include JPA annotations
    - Implement proper relationships
    - Include validation constraints

- **Use appropriate relationship mappings**:
    - `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`
    - Consider fetch types (LAZY vs EAGER) based on usage patterns

- **Implement equals() and hashCode()** properly for entities

### Repository Design

- **Extend JpaRepository** for standard CRUD operations:
  ```java
  public interface ProductRepository extends JpaRepository<Product, Long> {
  }
  ```

- **Use derived query methods** for simple queries:
  ```java
  List<Product> findByCategory_CategoryId(Long categoryId);
  ```

- **Use @Query for complex queries**:
  ```java
  @Query("SELECT p FROM Product p WHERE p.price > :minPrice AND p.category.name = :category")
  List<Product> findExpensiveProductsByCategory(@Param("minPrice") BigDecimal minPrice, @Param("category") String category);
  ```

- **Use Specification API** for dynamic queries

### Data Transfer Objects (DTOs)

- **Use DTOs to transfer data between layers**:
    - Protect entity internals
    - Include only necessary fields
    - Implement validation at the DTO level

- **Use mapping libraries** like ModelMapper or MapStruct:
  ```java
  @Bean
  public ModelMapper modelMapper() {
      return new ModelMapper();
  }
  ```

## Service Layer

### Service Design

- **Define services with interfaces**:
  ```java
  public interface ProductService {
      ProductDTO getProductById(Long id);
      ProductResponse getAllProducts(int page, int size, String sortBy, String sortDir);
      // ...
  }
  ```

- **Implement transaction management**:
  ```java
  @Service
  @Transactional
  public class ProductServiceImpl implements ProductService {
      // ...
  }
  ```

- **Use constructor injection** for dependencies:
  ```java
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
      this.productRepository = productRepository;
      this.modelMapper = modelMapper;
  }
  ```

### Business Logic

- **Keep business logic in services**, not in controllers or repositories
- **Validate inputs** at the service level
- **Handle transactions** appropriately
- **Log important operations** and errors

### Pagination

- **Implement pagination for all list endpoints**:
  ```java
  public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
      Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                                                      : Sort.by(sortBy).descending();
      
      Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
      Page<Product> productPage = productRepository.findAll(pageable);
      
      // Convert to response...
  }
  ```

## Exception Handling

### Global Exception Handling

- **Implement a global exception handler**:
  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {
      @ExceptionHandler(ResourceNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
          // ...
      }
      
      @ExceptionHandler(Exception.class)
      public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
          // ...
      }
  }
  ```

### Custom Exceptions

- **Create custom exceptions** for different error scenarios:
  ```java
  public class ResourceNotFoundException extends RuntimeException {
      private String resourceName;
      private String fieldName;
      private Object fieldValue;
      
      // Constructor and getters...
  }
  ```

### Validation

- **Use Bean Validation** (Jakarta Validation):
  ```java
  public class ProductDTO {
      @NotBlank(message = "Product name cannot be blank")
      @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
      private String name;
      
      @NotNull(message = "Price cannot be null")
      @Positive(message = "Price must be positive")
      private BigDecimal price;
      
      // ...
  }
  ```

- **Validate request bodies** in controllers:
  ```java
  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
      // ...
  }
  ```

## Security

### Authentication and Authorization

- **Use Spring Security** for authentication and authorization:
  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/public/**").permitAll()
                  .requestMatchers("/api/admin/**").hasRole("ADMIN")
                  .anyRequest().authenticated()
              )
              .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
          
          return http.build();
      }
  }
  ```

### JWT Authentication

- **Implement JWT for stateless authentication**:
    - Use refresh tokens for better security
    - Set appropriate token expiration times
    - Store sensitive user data in secure storage, not in JWTs

### Input Validation

- **Always validate and sanitize inputs** to prevent injection attacks
- **Use parameterized queries** for database operations
- **Escape user-generated content** before displaying it

## Configuration

### Properties Management

- **Use application.properties or application.yml** for configuration:
  ```properties
  # Database
  spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
  spring.datasource.username=${DB_USERNAME}
  spring.datasource.password=${DB_PASSWORD}
  
  # JPA
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  
  # File Storage
  app.file.upload-dir=./uploads
  ```

- **Use profiles for different environments**:
  ```properties
  spring.profiles.active=dev
  ```

- **Externalize sensitive configuration** using environment variables or secure vaults

### Bean Configuration

- **Use Java configuration** for beans:
  ```java
  @Configuration
  public class AppConfig {
      @Bean
      public ModelMapper modelMapper() {
          return new ModelMapper();
      }
  }
  ```

- **Use conditional beans** for different environments:
  ```java
  @Bean
  @Profile("dev")
  public EmailService devEmailService() {
      return new MockEmailService();
  }
  
  @Bean
  @Profile("prod")
  public EmailService prodEmailService() {
      return new SmtpEmailService();
  }
  ```

## Testing

### Unit Testing

- **Test each layer independently**:
    - Repository tests with `@DataJpaTest`
    - Service tests with mocked repositories
    - Controller tests with `@WebMvcTest`

- **Use JUnit 5 and Mockito**:
  ```java
  @ExtendWith(MockitoExtension.class)
  class ProductServiceTest {
      @Mock
      private ProductRepository productRepository;
      
      @InjectMocks
      private ProductServiceImpl productService;
      
      @Test
      void shouldReturnProductWhenProductExists() {
          // Arrange
          Long id = 1L;
          Product product = new Product();
          product.setProductId(id);
          when(productRepository.findById(id)).thenReturn(Optional.of(product));
          
          // Act
          ProductDTO result = productService.getProductById(id);
          
          // Assert
          assertNotNull(result);
          assertEquals(id, result.getId());
      }
  }
  ```

### Integration Testing

- **Use @SpringBootTest for integration tests**:
  ```java
  @SpringBootTest
  @AutoConfigureMockMvc
  class ProductControllerIntegrationTest {
      @Autowired
      private MockMvc mockMvc;
      
      @Test
      void shouldReturnProductWhenGetProductById() throws Exception {
          mockMvc.perform(get("/api/products/1"))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(1));
      }
  }
  ```

- **Use test containers for database tests**:
  ```java
  @SpringBootTest
  @Testcontainers
  class ProductRepositoryTest {
      @Container
      static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0");
      
      @DynamicPropertySource
      static void registerMySQLProperties(DynamicPropertyRegistry registry) {
          registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
          registry.add("spring.datasource.username", mySQLContainer::getUsername);
          registry.add("spring.datasource.password", mySQLContainer::getPassword);
      }
      
      @Autowired
      private ProductRepository productRepository;
      
      @Test
      void shouldSaveProduct() {
          // Test logic...
      }
  }
  ```

## Performance

### Database Optimization

- **Use appropriate indexes** for frequently queried fields
- **Implement pagination** for large result sets
- **Use projections** for partial data retrieval:
  ```java
  public interface ProductSummary {
      Long getId();
      String getName();
      BigDecimal getPrice();
  }
  
  List<ProductSummary> findAllProjectedBy();
  ```

### Caching

- **Implement caching for frequently accessed data**:
  ```java
  @EnableCaching
  @Configuration
  public class CacheConfig {
      @Bean
      public CacheManager cacheManager() {
          return new ConcurrentMapCacheManager("products", "categories");
      }
  }
  
  @Service
  public class ProductServiceImpl implements ProductService {
      @Cacheable("products")
      public ProductDTO getProductById(Long id) {
          // ...
      }
      
      @CacheEvict(value = "products", key = "#id")
      public void deleteProduct(Long id) {
          // ...
      }
  }
  ```

### Lazy Loading

- **Use lazy loading for entity relationships** to avoid N+1 select problems:
  ```java
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Product> products;
  ```

- **Use join fetches for specific queries** that need related data:
  ```java
  @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
  Optional<Category> findByIdWithProducts(@Param("id") Long id);
  ```

## Deployment

### Build Process

- **Use Maven or Gradle** for building the application:
  ```xml
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>
  ```

- **Create a standalone JAR** that includes all dependencies:
  ```bash
  mvn clean package
  ```

### Containerization

- **Use Docker** for containerization:
  ```dockerfile
  FROM eclipse-temurin:17-jre-alpine
  VOLUME /tmp
  ARG JAR_FILE=target/*.jar
  COPY ${JAR_FILE} app.jar
  ENTRYPOINT ["java", "-jar", "/app.jar"]
  ```

- **Use docker-compose** for local development:
  ```yaml
  version: '3'
  services:
    app:
      build: .
      ports:
        - "8080:8080"
      depends_on:
        - db
      environment:
        - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/ecommerce
        - SPRING_DATASOURCE_USERNAME=root
        - SPRING_DATASOURCE_PASSWORD=password
    
    db:
      image: mysql:8.0
      ports:
        - "3306:3306"
      environment:
        - MYSQL_ROOT_PASSWORD=password
        - MYSQL_DATABASE=ecommerce
  ```

### Monitoring

- **Implement Spring Boot Actuator** for health checks and monitoring:
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

- **Configure Actuator endpoints**:
  ```properties
  management.endpoints.web.exposure.include=health,info,metrics
  management.endpoint.health.show-details=always
  ```

- **Use Prometheus and Grafana** for metrics collection and visualization:
  ```xml
  <dependency>
      <groupId>io.micrometer</groupId>
      <artifactId>micrometer-registry-prometheus</artifactId>
  </dependency>
  ```
