package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.category.CategoryDTO;
import com.upskilldev.ordersystem.dto.category.CreateCategoryDTO;
import com.upskilldev.ordersystem.dto.category.UpdateCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO);
    CategoryDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO);
    void deleteCategory(Long id);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();
}
