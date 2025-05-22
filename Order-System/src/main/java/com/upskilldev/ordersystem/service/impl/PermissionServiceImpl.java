package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.permission.CreatePermissionDTO;
import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import com.upskilldev.ordersystem.dto.permission.UpdatePermissionDTO;
import com.upskilldev.ordersystem.entity.Permission;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.PermissionMapper;
import com.upskilldev.ordersystem.repository.PermissionRepository;
import com.upskilldev.ordersystem.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionDTO createPermission(CreatePermissionDTO createPermissionDTO) {
        Permission permission = new Permission();
        permission.setName(createPermissionDTO.getName());
        permission.setDescription(createPermissionDTO.getDescription());

        return PermissionMapper.toDTO(permissionRepository.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(Long permissionId, UpdatePermissionDTO updatePermissionDTO) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId));
        permission.setName(updatePermissionDTO.getName());
        permission.setDescription(updatePermissionDTO.getDescription());

        return PermissionMapper.toDTO(permissionRepository.save(permission));
    }

    @Override
    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId));
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDTO getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId));
        return PermissionMapper.toDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(PermissionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
