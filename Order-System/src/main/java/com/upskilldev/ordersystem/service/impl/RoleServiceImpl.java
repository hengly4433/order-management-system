package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.role.CreateRoleDTO;
import com.upskilldev.ordersystem.dto.role.RoleDTO;
import com.upskilldev.ordersystem.dto.role.UpdateRoleDTO;
import com.upskilldev.ordersystem.entity.Permission;
import com.upskilldev.ordersystem.entity.Role;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.RoleMapper;
import com.upskilldev.ordersystem.repository.PermissionRepository;
import com.upskilldev.ordersystem.repository.RoleRepository;
import com.upskilldev.ordersystem.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public RoleDTO createRole(CreateRoleDTO createRoleDTO) {
        log.debug("Service: Creating role: {}", createRoleDTO.getName());
        Role role = new Role();
        role.setName(createRoleDTO.getName());
        Set<Permission> permissions = createRoleDTO.getPermissions().stream()
                .map(pdto -> permissionRepository.findById(pdto.getId())
                        .orElseThrow(() -> {
                            log.error("Service: Permission not found for assign, ID: {}", pdto.getId());
                            return new ResourceNotFoundException("Permission not found with id: " + pdto.getId());
                        }))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        RoleDTO dto = RoleMapper.toDTO(roleRepository.save(role));
        log.info("Service: Role created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long roleId, UpdateRoleDTO updateRoleDTO) {
        log.debug("Service: Updating role ID: {}", roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Service: Role not found for update, ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        role.setName(updateRoleDTO.getName());
        Set<Permission> permissions = updateRoleDTO.getPermissions().stream()
                .map(pdto -> permissionRepository.findById(pdto.getId())
                        .orElseThrow(() -> {
                            log.error("Service: Permission not found for update assign, ID: {}", pdto.getId());
                            return new ResourceNotFoundException("Permission not found with id: " + pdto.getId());
                        }))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        RoleDTO dto = RoleMapper.toDTO(roleRepository.save(role));
        log.info("Service: Updated role ID: {}", dto.getId());
        return dto;
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        log.warn("Service: Deleting role ID: {}", roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Service: Role not found for delete, ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        roleRepository.delete(role);
        log.info("Service: Deleted role ID: {}", roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(Long roleId) {
        log.debug("Service: Retrieving role by ID: {}", roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Service: Role not found, ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        RoleDTO dto = RoleMapper.toDTO(role);
        log.info("Service: Retrieved role ID: {}", roleId);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        log.debug("Service: Retrieving all roles");
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total roles retrieved: {}", roles.size());
        return roles;
    }

    @Override
    @Transactional
    public RoleDTO assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        log.info("Service: Assigning permissions {} to role ID: {}", permissionIds, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Service: Role not found for permission assignment, ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        Set<Permission> permissions = permissionIds.stream()
                .map(pid -> permissionRepository.findById(pid)
                        .orElseThrow(() -> {
                            log.error("Service: Permission not found for assign, ID: {}", pid);
                            return new ResourceNotFoundException("Permission not found with id: " + pid);
                        }))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        RoleDTO dto = RoleMapper.toDTO(roleRepository.save(role));
        log.info("Service: Assigned permissions to role ID: {}", roleId);
        return dto;
    }

    @Override
    @Transactional
    public RoleDTO removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        log.info("Service: Removing permissions {} from role ID: {}", permissionIds, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Service: Role not found for permission removal, ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        Set<Permission> permissions = permissionIds.stream()
                .map(pid -> permissionRepository.findById(pid)
                        .orElseThrow(() -> {
                            log.error("Service: Permission not found for remove, ID: {}", pid);
                            return new ResourceNotFoundException("Permission not found with id: " + pid);
                        }))
                .collect(Collectors.toSet());
        role.getPermissions().removeAll(permissions);
        RoleDTO dto = RoleMapper.toDTO(roleRepository.save(role));
        log.info("Service: Removed permissions from role ID: {}", roleId);
        return dto;
    }
}
