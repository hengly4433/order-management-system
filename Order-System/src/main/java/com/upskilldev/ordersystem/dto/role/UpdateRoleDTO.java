package com.upskilldev.ordersystem.dto.role;

import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class UpdateRoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    private String name;

    @NotBlank(message = "Permission is required")
    private Set<PermissionDTO> permissions;

    public UpdateRoleDTO() {
    }

    public UpdateRoleDTO(String name, Set<PermissionDTO> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Role name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Role name is required") String name) {
        this.name = name;
    }

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
