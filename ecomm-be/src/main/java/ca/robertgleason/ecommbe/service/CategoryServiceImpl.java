package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.excepetions.ResourceNotFoundException;
import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the CategoryService interface.
 * <p>
 * Implementation practices:
 * - Exception handling with meaningful messages
 * - Logging for operations and errors
 * - Input validation
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * Logger for this service
     */
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
//    private final List<Category> categories = new ArrayList<>();

    /**
     * Repository for database operations
     */
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Attempted to delete non-existent category with ID: {}", categoryId);
            throw new ResourceNotFoundException("Category", "ID", categoryId);
        }
        categoryRepository.deleteById(categoryId);
        log.info("Category with ID {} deleted successfully", categoryId);
        return "Category with ID " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Attempted to update non-existent category with ID: {}", categoryId);
                    return new ResourceNotFoundException("Category", "ID", categoryId);
                });
        existingCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category with ID {} updated successfully", categoryId);
        return updatedCategory;
    }


}
