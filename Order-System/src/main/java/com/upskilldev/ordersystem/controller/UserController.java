package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.user.CreateUserDTO;
import com.upskilldev.ordersystem.dto.user.UpdateUserDTO;
import com.upskilldev.ordersystem.dto.user.UserDTO;
import com.upskilldev.ordersystem.service.UserService;
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
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Admin operations for managing users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create User", description = "Create a new user")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.ok(userService.createUser(createUserDTO));
    }

    @Operation(summary = "Get User", description = "Retrieve a user by ID")
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(summary = "Get All Users", description = "Retrieve all users")
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Update User", description = "Update an existing user")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, updateUserDTO));
    }

    @Operation(summary = "Delete User", description = "Delete a user by ID")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign Role To User", description = "Adds additional roles to a user without removing existing roles")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE_TO_USER')")
    @PostMapping("/{userId}/roles/assign")
    public ResponseEntity<UserDTO> assignRoleToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        return ResponseEntity.ok(userService.assignRoleToUser(userId, roleIds));
    }

    @Operation(summary = "Remove Role From User", description = "Removes specific roles from a user")
    @PreAuthorize("hasAuthority('REMOVE_ROLE_FROM_USER')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserDTO> removeRoleFromUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        return ResponseEntity.ok(userService.removeRoleFromUsers(userId, roleIds));
    }
}
