package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        // this method creates a new category and adds it to the list
        Long newId = (long) (categories.size() + 1);

        category.setCategoryId(newId);
        categories.add(category);


    }

    @Override
    public String deleteCategory(Long categoryId) {
        // this method deletes a category by its ID
        boolean exists = categories.stream()
                .anyMatch(category -> category.getCategoryId().equals(categoryId));

        // this checks if the category exists before attempting to delete it
        if (!exists) {
            log.warn("Attempted to delete non-existent category with ID: {}", categoryId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + categoryId);
        }

        // this removes the category from the list
        categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        log.info("Category with ID {} deleted successfully", categoryId);
        throw new ResponseStatusException(HttpStatus.OK, "Category deleted successfully");
    }

    @Override
    public void updateCategory(Long categoryId, Category category) {
        // this method updates an existing category
        boolean exists = categories.stream()
                // this checks if the category with the given ID exists
                .anyMatch(existingCategory -> existingCategory.getCategoryId().equals(categoryId));
        if (!exists) {
            log.warn("Attempted to update non-existent category with ID: {}", categoryId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + categoryId);
        }
        // this updates the category if it exists
        categories.stream()
                // this finds the category with the given ID
                .filter(existingCategory -> existingCategory.getCategoryId().equals(categoryId))
                .findFirst()
                .ifPresent(existingCategory -> {
                    existingCategory.setCategoryName(category.getCategoryName());
                    log.info("Category with ID {} updated successfully", categoryId);
                });
        throw new ResponseStatusException(HttpStatus.OK, "Category updated successfully");
    }

}
