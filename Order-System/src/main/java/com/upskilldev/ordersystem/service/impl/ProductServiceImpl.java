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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

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

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file must be provided");
        }

        Category category = categoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + createProductDTO.getCategoryId()));

        // 2) save image to disk
        String filename = fileStorage.storeFile(imageFile);
        String url      = "/images/" + filename;

        Product product = ProductMapper.toEntity(createProductDTO, category);
        product.setImageUrl(url);
        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        Category category = categoryRepository.findById(updateProductDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updateProductDTO.getCategoryId()));
        ProductMapper.updateEntity(updateProductDTO, product, category);
        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return ProductMapper.toDTO(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id)));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }
}
