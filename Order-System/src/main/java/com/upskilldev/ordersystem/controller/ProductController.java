package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.product.CreateProductDTO;
import com.upskilldev.ordersystem.dto.product.ProductDTO;
import com.upskilldev.ordersystem.dto.product.UpdateProductDTO;
import com.upskilldev.ordersystem.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "CRUD for Products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create Product")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductDTO> create(@Valid @ModelAttribute CreateProductDTO createProductDTO,
                                             @RequestPart("imageFile") MultipartFile imageFile) {
        log.info("Creating product: {} in category ID: {}", createProductDTO.getName(), createProductDTO.getCategoryId());
        ProductDTO result = productService.createProduct(createProductDTO, imageFile);
        log.info("Created product with ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "List Products")
    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> list() {
        log.debug("Listing all products");
        List<ProductDTO> result = productService.getAllProducts();
        log.info("Total products retrieved: {}", result.size());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Product")
    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        log.debug("Retrieving product by ID: {}", id);
        ProductDTO result = productService.getProductById(id);
        log.info("Retrieved product: {}", result.getName());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update Product")
    @PreAuthorize("hasAuthority('EDIT_PRODUCT')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO updateProductDTO) {
        log.info("Updating product ID: {}", id);
        ProductDTO result = productService.updateProduct(id, updateProductDTO);
        log.info("Updated product ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete Product")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("Deleting product ID: {}", id);
        productService.deleteProduct(id);
        log.info("Deleted product with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
