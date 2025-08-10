package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.excepetions.APIException;
import ca.robertgleason.ecommbe.excepetions.ResourceNotFoundException;
import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.payload.CategoryDTO;
import ca.robertgleason.ecommbe.payload.CategoryResponse;
import ca.robertgleason.ecommbe.repository.CategoryRepository;
import ca.robertgleason.ecommbe.utilties.MappingUtils;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);


    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MappingUtils mappingUtils;


    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, MappingUtils mappingUtils) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.mappingUtils = mappingUtils;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }

        List<CategoryDTO> categoryDTOs = mappingUtils.mapList(categories, CategoryDTO.class);

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setCategories(categoryDTOs);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(@Valid CategoryDTO categoryDTO) {
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
