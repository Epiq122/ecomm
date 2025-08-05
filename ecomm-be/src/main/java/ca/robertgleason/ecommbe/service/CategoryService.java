package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(Category category);

    String deleteCategory(Long categoryId);


    Category updateCategory(Long categoryId, Category category);


}
