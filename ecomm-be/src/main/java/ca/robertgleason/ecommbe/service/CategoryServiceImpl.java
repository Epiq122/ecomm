package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final List<Category> categories = new ArrayList<>();


    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Long newId = (long) (categories.size() + 1);

        category.setCategoryId(newId);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        boolean exists = categories.stream()
                .anyMatch(category -> category.getCategoryId().equals(categoryId));

        if (!exists) {
            log.warn("Attempted to delete non-existent category with ID: {}", categoryId);
            return "Category with ID " + categoryId + " not found";
        }

        categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        log.info("Category with ID {} deleted successfully", categoryId);
        return "Category with ID " + categoryId + " deleted";
    }

}
