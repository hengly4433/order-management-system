package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.category.CategoryDTO;
import com.upskilldev.ordersystem.dto.category.CreateCategoryDTO;
import com.upskilldev.ordersystem.dto.category.UpdateCategoryDTO;
import com.upskilldev.ordersystem.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "CRUD for Categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create Category")
    @PreAuthorize("hasAuthority('CREATE_CATEGORY')")
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        log.info("Creating category: {}", createCategoryDTO.getName());
        CategoryDTO result = categoryService.createCategory(createCategoryDTO);
        log.info("Created category with ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "List Categories")
    @PreAuthorize("hasAuthority('VIEW_CATEGORY')")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> list() {
        log.debug("Listing all categories");
        List<CategoryDTO> result = categoryService.getAllCategories();
        log.info("Total categories retrieved: {}", result.size());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Category")
    @PreAuthorize("hasAuthority('VIEW_CATEGORY')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
        log.debug("Retrieving category by ID: {}", id);
        CategoryDTO result = categoryService.getCategoryById(id);
        log.info("Retrieved category: {}", result.getName());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update Category")
    @PreAuthorize("hasAuthority('EDIT_CATEGORY')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        log.info("Updating category ID: {}", id);
        CategoryDTO result = categoryService.updateCategory(id, updateCategoryDTO);
        log.info("Updated category ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete Category")
    @PreAuthorize("hasAuthority('DELETE_CATEGORY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("Deleting category ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Deleted category with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
