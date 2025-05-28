package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.product.CreateProductDTO;
import com.upskilldev.ordersystem.dto.product.ProductDTO;
import com.upskilldev.ordersystem.dto.product.UpdateProductDTO;
import com.upskilldev.ordersystem.entity.Category;
import com.upskilldev.ordersystem.entity.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setStock(product.getStock());
        productDTO.setSku(product.getSku());
        productDTO.setReorderThreshold(product.getReorderThreshold());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setCategoryId(product.getCategory().getId());
        return productDTO;
    }

    public static Product toEntity(CreateProductDTO productDTO, Category category) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setSku(productDTO.getSku());
        product.setReorderThreshold(productDTO.getReorderThreshold());
        product.setCategory(category);
        return product;
    }

    public static void updateEntity(UpdateProductDTO updateProductDTO, Product product, Category category) {
        product.setName(updateProductDTO.getName());
        product.setDescription(updateProductDTO.getDescription());
        product.setPrice(updateProductDTO.getPrice());
        product.setStock(updateProductDTO.getStock());
        product.setSku(updateProductDTO.getSku());
        product.setReorderThreshold(updateProductDTO.getReorderThreshold());
        product.setImageUrl(updateProductDTO.getImageUrl());
        product.setCategory(category);
    }


}
