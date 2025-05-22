package com.upskilldev.ordersystem.dto.permission;

import jakarta.validation.constraints.NotBlank;

public class CreatePermissionDTO {

    @NotBlank(message = "Permission name is required")
    private String name;

    private String description;

    public CreatePermissionDTO() {
    }

    public CreatePermissionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public @NotBlank(message = "Permission name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Permission name is required") String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
