package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
//    private final List<Category> categories = new ArrayList<>();


    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    // this no longer used the long id it is updated from the entity now

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        // this method deletes a category by its ID
        boolean exists = categories.stream()
                .anyMatch(category -> category.getCategoryId().equals(categoryId));

        // this checks if the category exists before attempting to delete it
        if (!exists) {
            log.warn("Attempted to delete non-existent category with ID: {}", categoryId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + categoryId);
        }
        // this uses the repository to delete the category
        categoryRepository.deleteById(categoryId);
        log.info("Category with ID {} deleted successfully", categoryId);
        return "Category with ID " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        List<Category> categories = categoryRepository.findAll();

        Optional<Category> existingCategoryOpt = categories.stream()
                .filter(cat -> cat.getCategoryId().equals(categoryId))
                .findFirst();

        if (existingCategoryOpt.isPresent()) {
            Category existingCategory = existingCategoryOpt.get();
            existingCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(existingCategory);
            log.info("Category with ID {} updated successfully", categoryId);
        } else {
            log.warn("Attempted to update non-existent category with ID: {}", categoryId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + categoryId);
        }
        return category;
    }

}
