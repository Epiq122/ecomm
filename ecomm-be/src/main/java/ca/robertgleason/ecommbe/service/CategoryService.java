package ca.robertgleason.ecommbe.service;

import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.payload.CategoryResponse;
import org.springframework.stereotype.Service;

/**
 * Service interface for Category operations.
 * <p>
 * Benefits of service interfaces:
 * - Loose coupling
 * - Easier testing with mocks
 * - Clear contract definition
 */
@Service
public interface CategoryService {
    /**
     * Get all categories
     */
    CategoryResponse getAllCategories();

    /**
     * Create a new category
     */
    Category createCategory(Category category);

    /**
     * Delete a category by ID
     *
     * @throws org.springframework.web.server.ResponseStatusException if not found
     */
    String deleteCategory(Long categoryId);

    /**
     * Update an existing category
     *
     * @throws org.springframework.web.server.ResponseStatusException if not found
     */
    Category updateCategory(Long categoryId, Category category);
}
