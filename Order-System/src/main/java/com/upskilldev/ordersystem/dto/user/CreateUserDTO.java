package com.upskilldev.ordersystem.dto.user;

import com.upskilldev.ordersystem.dto.role.RoleDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class CreateUserDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private Set<RoleDTO> roles;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String email, String password, Set<RoleDTO> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public @NotBlank(message = "Username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Role is required") Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(@NotBlank(message = "Role is required") Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
