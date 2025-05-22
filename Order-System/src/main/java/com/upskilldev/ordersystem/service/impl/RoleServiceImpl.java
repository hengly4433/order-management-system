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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public RoleDTO createRole(CreateRoleDTO createRoleDTO) {
        Role role = new Role();
        role.setName(createRoleDTO.getName());
        Set<Permission> permissions = createRoleDTO.getPermissions().stream()
                .map(pdto -> permissionRepository.findById(pdto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found")))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        return RoleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long roleId, UpdateRoleDTO updateRoleDTO) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        role.setName(updateRoleDTO.getName());
        Set<Permission> permissions = updateRoleDTO.getPermissions().stream()
                .map(pdto -> permissionRepository.findById(pdto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found")))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        return RoleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        return RoleMapper.toDTO(role);
    }

    @Override
    @Transactional
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDTO assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<Permission> permissions = permissionIds.stream()
                .map(pid -> permissionRepository.findById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not founmd")))
                .collect(Collectors.toSet());
        role.setPermissions(permissions);
        return RoleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<Permission> permissions = permissionIds.stream()
                .map(pid -> permissionRepository.findById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found")))
                .collect(Collectors.toSet());
        role.getPermissions().removeAll(permissions);
        return RoleMapper.toDTO(roleRepository.save(role));
    }
}
