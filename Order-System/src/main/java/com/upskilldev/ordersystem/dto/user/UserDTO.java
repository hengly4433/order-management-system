package com.upskilldev.ordersystem.dto.user;

import com.upskilldev.ordersystem.dto.role.RoleDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class UserDTO {

    private Long id;
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email is required")
    private String email;

    private Set<RoleDTO> roles;

    public UserDTO() {
    }

    public UserDTO(String username, String email, Set<RoleDTO> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") String username) {
        this.username = username;
    }

    public @Email(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email is required") String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
