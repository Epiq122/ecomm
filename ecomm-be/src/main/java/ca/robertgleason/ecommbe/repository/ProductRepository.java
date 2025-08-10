package ca.robertgleason.ecommbe.repository;

import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);

    Product findByProductName(@NotBlank @Size(min = 3, message = "Product name must be at least 3 characters long") String productName);
}
