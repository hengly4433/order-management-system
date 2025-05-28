package com.upskilldev.ordersystem.dto.category;

import jakarta.validation.constraints.NotBlank;

public class UpdateCategoryDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    public UpdateCategoryDTO() {
    }

    public UpdateCategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
