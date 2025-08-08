package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Product;
import ca.robertgleason.ecommbe.payload.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);
}
