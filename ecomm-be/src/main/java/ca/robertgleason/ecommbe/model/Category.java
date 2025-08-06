package ca.robertgleason.ecommbe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category entity representing product categories in the e-commerce system.
 * <p>
 * Key annotations:
 * - @Entity: JPA entity
 * - @Data: Lombok for getters, setters, equals, hashCode, toString
 * - @NoArgsConstructor/@AllArgsConstructor: Lombok for constructors
 */
@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Primary key using database auto-increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    /**
     * Category name with validation
     */
    @NotBlank
    @Size(min = 5, message = "Category name must be at least 5 characters long")
    private String categoryName;
}
