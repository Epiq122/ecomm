package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.excepetions.APIException;
import ca.robertgleason.ecommbe.excepetions.ResourceNotFoundException;
import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.payload.CategoryDTO;
import ca.robertgleason.ecommbe.payload.CategoryResponse;
import ca.robertgleason.ecommbe.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        return new CategoryResponse(categoryDTOs);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIException("Category with name '" + category.getCategoryName() + "' already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Attempted to delete non-existent category with ID: {}", categoryId);
            throw new ResourceNotFoundException("Category", "ID", categoryId);
        }
        categoryRepository.deleteById(categoryId);
        log.info("Category with ID {} deleted successfully", categoryId);
        return new CategoryDTO(categoryId, "Category deleted successfully");
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Attempted to update non-existent category with ID: {}", categoryId);
                    return new ResourceNotFoundException("Category", "ID", categoryId);
                });
        Category category = modelMapper.map(categoryDTO, Category.class);
        existingCategory.setCategoryName(category.getCategoryName());
        return modelMapper.map(categoryRepository.save(existingCategory), CategoryDTO.class);
    }


}
