package ca.robertgleason.ecommbe.controller;

import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for handling Category-related HTTP requests.
 * <p>
 * Design principles:
 * - Thin controllers (business logic in services)
 * - Appropriate HTTP methods and status codes
 * - Public vs. admin endpoint separation
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructor injection for dependencies
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories
     */
    @GetMapping("/public/categories")
    public List<Category> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK).getBody();
    }

    /**
     * Create a new category with validation
     */
    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    /**
     * Delete category by ID (admin only)
     */
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    /**
     * Update existing category
     */
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
            categoryService.updateCategory(categoryId, category);
            return ResponseEntity.ok("Category updated successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
