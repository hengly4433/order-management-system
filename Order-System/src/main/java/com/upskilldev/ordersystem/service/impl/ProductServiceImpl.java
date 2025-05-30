package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.product.CreateProductDTO;
import com.upskilldev.ordersystem.dto.product.ProductDTO;
import com.upskilldev.ordersystem.dto.product.UpdateProductDTO;
import com.upskilldev.ordersystem.entity.Category;
import com.upskilldev.ordersystem.entity.Product;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.ProductMapper;
import com.upskilldev.ordersystem.repository.CategoryRepository;
import com.upskilldev.ordersystem.repository.ProductRepository;
import com.upskilldev.ordersystem.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorage;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, FileStorageService fileStorage) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorage = fileStorage;
    }

    @Override
    public ProductDTO createProduct(CreateProductDTO createProductDTO, MultipartFile imageFile) {
        log.debug("Service: Creating product: {} in category ID: {}", createProductDTO.getName(), createProductDTO.getCategoryId());
        if (imageFile == null || imageFile.isEmpty()) {
            log.error("Service: Image file must be provided for product: {}", createProductDTO.getName());
            throw new IllegalArgumentException("Image file must be provided");
        }

        Category category = categoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Service: Category not found for create, ID: {}", createProductDTO.getCategoryId());
                    return new ResourceNotFoundException("Category not found with id: " + createProductDTO.getCategoryId());
                });

        String filename = fileStorage.storeFile(imageFile);
        String url = "/images/" + filename;

        Product product = ProductMapper.toEntity(createProductDTO, category);
        product.setImageUrl(url);
        ProductDTO dto = ProductMapper.toDTO(productRepository.save(product));
        log.info("Service: Product created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        log.debug("Service: Updating product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Product not found for update, ID: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        Category category = categoryRepository.findById(updateProductDTO.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Service: Category not found for update, ID: {}", updateProductDTO.getCategoryId());
                    return new ResourceNotFoundException("Category not found with id: " + updateProductDTO.getCategoryId());
                });

        ProductMapper.updateEntity(updateProductDTO, product, category);
        ProductDTO dto = ProductMapper.toDTO(productRepository.save(product));
        log.info("Service: Updated product ID: {}", dto.getId());
        return dto;
    }

    @Override
    public void deleteProduct(Long id) {
        log.warn("Service: Deleting product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Product not found for delete, ID: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });
        productRepository.delete(product);
        log.info("Service: Deleted product ID: {}", id);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        log.debug("Service: Retrieving product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Product not found, ID: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });
        ProductDTO dto = ProductMapper.toDTO(product);
        log.info("Service: Retrieved product ID: {}", id);
        return dto;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.debug("Service: Retrieving all products");
        List<ProductDTO> products = productRepository.findAll()
                .stream().map(ProductMapper::toDTO).collect(Collectors.toList());
        log.info("Service: Total products retrieved: {}", products.size());
        return products;
    }
}
