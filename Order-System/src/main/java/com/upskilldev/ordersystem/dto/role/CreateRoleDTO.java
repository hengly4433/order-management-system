package com.upskilldev.ordersystem.dto.role;

import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class CreateRoleDTO {

    @NotBlank(message = "Role name is required")
    private String name;

    @NotNull(message = "Permission is required")
    private Set<PermissionDTO> permissions;

    public CreateRoleDTO() {
    }

    public CreateRoleDTO(String name, Set<PermissionDTO> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
