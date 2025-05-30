package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto.permission.CreatePermissionDTO;
import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import com.upskilldev.ordersystem.dto.permission.UpdatePermissionDTO;
import com.upskilldev.ordersystem.service.PermissionService;
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
@RequestMapping("/api/permissions")
@Tag(name = "Permission Management", description = "Admin operations for managing permissions")
public class PermissionController {

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "Create Permission", description = "Create a new permission")
//    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody CreatePermissionDTO createPermissionDTO) {
        log.info("Creating permission: {}", createPermissionDTO.getName());
        PermissionDTO result = permissionService.createPermission(createPermissionDTO);
        log.info("Created permission with ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Get Permission By Id", description = "Retrieve permission by ID")
//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long permissionId) {
        log.debug("Retrieving permission by ID: {}", permissionId);
        return ResponseEntity.ok(permissionService.getPermissionById(permissionId));
    }

    @Operation(summary = "List Permissions", description = "List all permissions")
//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        log.debug("Listing all permissions");
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Update Permission", description = "Update an existing permission")
//    @PreAuthorize("hasAuthority('EDIT_PERMISSION')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long permissionId, @Valid @RequestBody UpdatePermissionDTO updatePermissionDTO) {
        log.info("Updating permission ID: {}", permissionId);
        PermissionDTO result = permissionService.updatePermission(permissionId, updatePermissionDTO);
        log.info("Updated permission ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete Permission", description = "Delete a permission by ID")
//    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        log.warn("Deleting permission ID: {}", permissionId);
        permissionService.deletePermission(permissionId);
        log.info("Deleted permission with ID: {}", permissionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all permissions grouped by module & package")
//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    @GetMapping("/grouped")
    public ResponseEntity<List<PackageDTO>> getGroupedPermissions() {
        log.debug("GET /api/permissions/grouped");
        List<PackageDTO> grouped = permissionService.getPermissionsGroupedByModule();
        log.info("Returning {} packages with their modules and permissions", grouped.size());
        return ResponseEntity.ok(grouped);
    }
}
