package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.role.CreateRoleDTO;
import com.upskilldev.ordersystem.dto.role.RoleDTO;
import com.upskilldev.ordersystem.dto.role.UpdateRoleDTO;
import com.upskilldev.ordersystem.service.RoleService;
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
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "Admin operations for managing roles")
public class RoleController {

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Create Role", description = "Create a new role")
    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        return ResponseEntity.ok(roleService.createRole(createRoleDTO));
    }

    @Operation(summary = "Get Rol", description = "Retrieve role by ID")
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @Operation(summary = "Get Roles", description = "Retrieve all roles")
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Update Role", description = "Update an existing role")
    @PreAuthorize("hasAuthority('EDIT_ROLE')")
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        return ResponseEntity.ok(roleService.updateRole(roleId, updateRoleDTO));
    }

    @Operation(summary = "Delete Role", description = "Delete a role by ID")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign Permissions", description = "Assign permissions to a role")
    @PreAuthorize("hasAuthority('ASSIGN_PERMISSION_TO_ROLE')")
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleDTO> assignPermissionToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleId, permissionIds));
    }
}
