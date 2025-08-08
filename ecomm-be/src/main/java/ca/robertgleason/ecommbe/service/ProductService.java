package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Product;
import ca.robertgleason.ecommbe.payload.ProductDTO;
import ca.robertgleason.ecommbe.payload.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword);
}
