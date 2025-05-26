package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.permission.CreatePermissionDTO;
import com.upskilldev.ordersystem.dto.permission.PermissionDTO;
import com.upskilldev.ordersystem.dto.permission.UpdatePermissionDTO;
import com.upskilldev.ordersystem.entity.Permission;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.PermissionMapper;
import com.upskilldev.ordersystem.repository.PermissionRepository;
import com.upskilldev.ordersystem.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionDTO createPermission(CreatePermissionDTO createPermissionDTO) {
        log.debug("Service: Creating permission: {}", createPermissionDTO.getName());
        Permission permission = new Permission();
        permission.setName(createPermissionDTO.getName());
        permission.setDescription(createPermissionDTO.getDescription());

        PermissionDTO dto = PermissionMapper.toDTO(permissionRepository.save(permission));
        log.info("Service: Permission created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public PermissionDTO updatePermission(Long permissionId, UpdatePermissionDTO updatePermissionDTO) {
        log.debug("Service: Updating permission ID: {}", permissionId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> {
                    log.error("Service: Permission not found for update, ID: {}", permissionId);
                    return new ResourceNotFoundException("Permission not found with id: " + permissionId);
                });
        permission.setName(updatePermissionDTO.getName());
        permission.setDescription(updatePermissionDTO.getDescription());
        PermissionDTO dto = PermissionMapper.toDTO(permissionRepository.save(permission));
        log.info("Service: Updated permission ID: {}", dto.getId());
        return dto;
    }

    @Override
    public void deletePermission(Long permissionId) {
        log.warn("Service: Deleting permission ID: {}", permissionId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> {
                    log.error("Service: Permission not found for delete, ID: {}", permissionId);
                    return new ResourceNotFoundException("Permission not found with id: " + permissionId);
                });
        permissionRepository.delete(permission);
        log.info("Service: Deleted permission ID: {}", permissionId);
    }

    @Override
    public PermissionDTO getPermissionById(Long permissionId) {
        log.debug("Service: Retrieving permission by ID: {}", permissionId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> {
                    log.error("Service: Permission not found, ID: {}", permissionId);
                    return new ResourceNotFoundException("Permission not found with id: " + permissionId);
                });
        return PermissionMapper.toDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        log.debug("Service: Retrieving all permissions");
        return permissionRepository.findAll()
                .stream()
                .map(PermissionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
