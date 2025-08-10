package ca.robertgleason.ecommbe.repository;

import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Product findByProductName(@NotBlank @Size(min = 3, message = "Product name must be at least 3 characters long") String productName);

    boolean existsByProductNameContainingIgnoreCase(String keyword);
}
