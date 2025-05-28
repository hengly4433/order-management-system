package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.product.CreateProductDTO;
import com.upskilldev.ordersystem.dto.product.ProductDTO;
import com.upskilldev.ordersystem.dto.product.UpdateProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(CreateProductDTO createProductDTO, MultipartFile imageFile);
    ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
}
