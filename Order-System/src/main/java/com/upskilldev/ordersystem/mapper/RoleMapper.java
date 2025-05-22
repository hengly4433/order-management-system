package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.role.RoleDTO;
import com.upskilldev.ordersystem.entity.Role;

import java.util.HashSet;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setPermissions(role.getPermissions().stream()
                .map(PermissionMapper::toDTO)
                .collect(Collectors.toSet()));
        return dto;
    }
}
