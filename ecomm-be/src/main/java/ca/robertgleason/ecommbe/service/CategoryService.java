package ca.robertgleason.ecommbe.service;

import ca.robertgleason.ecommbe.payload.CategoryDTO;
import ca.robertgleason.ecommbe.payload.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
