package ca.robertgleason.ecommbe.service;


import ca.robertgleason.ecommbe.excepetions.APIException;
import ca.robertgleason.ecommbe.excepetions.ResourceNotFoundException;
import ca.robertgleason.ecommbe.model.Category;
import ca.robertgleason.ecommbe.model.Product;
import ca.robertgleason.ecommbe.payload.ProductDTO;
import ca.robertgleason.ecommbe.payload.ProductResponse;
import ca.robertgleason.ecommbe.repository.CategoryRepository;
import ca.robertgleason.ecommbe.repository.ProductRepository;
import ca.robertgleason.ecommbe.utilties.MappingUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MappingUtils mappingUtils;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper, MappingUtils mappingUtils, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.mappingUtils = mappingUtils;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Product existingProduct = productRepository.findByProductName(productDTO.getProductName());
        if (existingProduct != null) {
            throw new APIException("Product with name " + productDTO.getProductName() + " already exists.");
        }

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }


    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        if (productPage.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTOS = mappingUtils.mapList(productPage.getContent(), ProductDTO.class);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }


    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.isEmpty()) {
            throw new APIException(category.getCategoryName() + " category does not have any products");
        }
        List<ProductDTO> productDTOS = mappingUtils.mapList(products, ProductDTO.class);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = mappingUtils.mapList(products, ProductDTO.class);

        if (products.isEmpty()) {
            throw new APIException("Products not found with keyword: " + keyword);
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }


    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductDescription(product.getProductDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        existingProduct.setSpecialPrice(specialPrice);
        existingProduct.setQuantity(product.getQuantity());


        return modelMapper.map(productRepository.save(existingProduct), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));


        String filename = fileService.uploadImage(path, image);

        productFromDb.setImage(filename);

        Product updatedProduct = productRepository.save(productFromDb);

        return modelMapper.map(updatedProduct, ProductDTO.class);


    }


}

