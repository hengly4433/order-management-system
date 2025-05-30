package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto.permission.CreatePermissionDTO;
import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import com.upskilldev.ordersystem.dto.permission.UpdatePermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(CreatePermissionDTO createPermissionDTO);
    PermissionDTO updatePermission(Long permissionId, UpdatePermissionDTO updatePermissionDTO);
    void deletePermission(Long permissionId);
    PermissionDTO getPermissionById(Long permissionId);
    List<PermissionDTO> getAllPermissions();

    /**
     * Returns all packages, each with its modules and those modules’
     * permissions, ready for “select-permission” UIs.
     */
    List<PackageDTO> getPermissionsGroupedByModule();

}
