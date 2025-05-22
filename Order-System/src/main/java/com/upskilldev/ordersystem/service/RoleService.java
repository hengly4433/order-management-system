package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.role.CreateRoleDTO;
import com.upskilldev.ordersystem.dto.role.RoleDTO;
import com.upskilldev.ordersystem.dto.role.UpdateRoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO createRole(CreateRoleDTO createRoleDTO);
    RoleDTO updateRole(Long roleId, UpdateRoleDTO updateRoleDTO);
    void deleteRole(Long roleId);
    RoleDTO getRoleById(Long roleId);
    List<RoleDTO> getAllRoles();

    RoleDTO assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    RoleDTO removePermissionsFromRole(Long roleId, List<Long> permissionIds);
}
