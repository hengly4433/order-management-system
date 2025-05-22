package com.upskilldev.ordersystem.service;

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

}
