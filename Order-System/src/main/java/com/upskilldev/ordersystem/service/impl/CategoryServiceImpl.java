package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.category.CategoryDTO;
import com.upskilldev.ordersystem.dto.category.CreateCategoryDTO;
import com.upskilldev.ordersystem.dto.category.UpdateCategoryDTO;
import com.upskilldev.ordersystem.entity.Category;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.CategoryMapper;
import com.upskilldev.ordersystem.repository.CategoryRepository;
import com.upskilldev.ordersystem.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        log.debug("Service: Creating category: {}", createCategoryDTO.getName());
        Category category = CategoryMapper.toEntity(createCategoryDTO);
        CategoryDTO dto = CategoryMapper.toDTO(categoryRepository.save(category));
        log.info("Service: Category created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public CategoryDTO updateCategory(Long id, UpdateCategoryDTO updateCategoryDTO) {
        log.debug("Service: Updating category ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Category not found for update, ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });
        CategoryMapper.updateEntity(updateCategoryDTO, category);
        CategoryDTO dto = CategoryMapper.toDTO(categoryRepository.save(category));
        log.info("Service: Updated category ID: {}", dto.getId());
        return dto;
    }

    @Override
    public void deleteCategory(Long id) {
        log.warn("Service: Deleting category ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Category not found for delete, ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });
        categoryRepository.delete(category);
        log.info("Service: Deleted category ID: {}", id);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        log.debug("Service: Retrieving category by ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Service: Category not found, ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });
        CategoryDTO dto = CategoryMapper.toDTO(category);
        log.info("Service: Retrieved category ID: {}", id);
        return dto;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.debug("Service: Retrieving all categories");
        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream().map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total categories retrieved: {}", categories.size());
        return categories;
    }
}
