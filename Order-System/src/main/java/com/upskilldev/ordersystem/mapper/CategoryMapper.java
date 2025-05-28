package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.category.CategoryDTO;
import com.upskilldev.ordersystem.dto.category.CreateCategoryDTO;
import com.upskilldev.ordersystem.dto.category.UpdateCategoryDTO;
import com.upskilldev.ordersystem.entity.Category;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    public static Category toEntity(CreateCategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    public static void updateEntity(UpdateCategoryDTO updateCategoryDTO, Category category) {
        category.setName(updateCategoryDTO.getName());
        category.setDescription(updateCategoryDTO.getDescription());
    }
}
