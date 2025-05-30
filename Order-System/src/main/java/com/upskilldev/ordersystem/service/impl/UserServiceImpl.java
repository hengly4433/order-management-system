package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.user.CreateUserDTO;
import com.upskilldev.ordersystem.dto.user.UpdateUserDTO;
import com.upskilldev.ordersystem.dto.user.UserDTO;
import com.upskilldev.ordersystem.entity.Role;
import com.upskilldev.ordersystem.entity.User;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.UserMapper;
import com.upskilldev.ordersystem.repository.RoleRepository;
import com.upskilldev.ordersystem.repository.UserRepository;
import com.upskilldev.ordersystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        log.debug("Service: Creating user: {}", createUserDTO.getUsername());
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        Set<Role> roles = createUserDTO.getRoles().stream()
                .map(rid -> roleRepository.findById(rid.getId())
                        .orElseThrow(() -> {
                            log.error("Service: Role not found for assign, ID: {}", rid.getId());
                            return new ResourceNotFoundException("Role not found with id: " + rid.getId());
                        }))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        UserDTO dto = UserMapper.toDTO(userRepository.save(user));
        log.info("Service: User created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO) {
        log.debug("Service: Updating user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Service: User not found for update, ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        // Optionally remove the username check, or handle according to business logic
        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());
        Set<Role> roles = updateUserDTO.getRoles().stream()
                .map(rid -> roleRepository.findById(rid.getId())
                        .orElseThrow(() -> {
                            log.error("Service: Role not found for update, ID: {}", rid.getId());
                            return new ResourceNotFoundException("Role not found with id: " + rid.getId());
                        }))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        UserDTO dto = UserMapper.toDTO(userRepository.save(user));
        log.info("Service: Updated user ID: {}", dto.getId());
        return dto;
    }

    @Override
    public void deleteUser(Long userId) {
        log.warn("Service: Deleting user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Service: User not found for delete, ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        userRepository.delete(user);
        log.info("Service: Deleted user ID: {}", userId);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        log.debug("Service: Retrieving user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Service: User not found, ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        UserDTO dto = UserMapper.toDTO(user);
        log.info("Service: Retrieved user ID: {}", userId);
        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.debug("Service: Retrieving all users");
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total users retrieved: {}", users.size());
        return users;
    }

    @Override
    public UserDTO assignRoleToUser(Long userId, List<Long> roleIds) {
        log.info("Service: Assigning roles {} to user ID: {}", roleIds, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Service: User not found for role assignment, ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        Set<Role> roles = roleIds.stream()
                .map(rid -> roleRepository.findById(rid)
                        .orElseThrow(() -> {
                            log.error("Service: Role not found for assign, ID: {}", rid);
                            return new ResourceNotFoundException("Role not found with id: " + rid);
                        }))
                .collect(Collectors.toSet());
        user.getRoles().addAll(roles); // Add roles to existing
        UserDTO dto = UserMapper.toDTO(userRepository.save(user));
        log.info("Service: Assigned roles to user ID: {}", userId);
        return dto;
    }

    @Override
    public UserDTO removeRoleFromUsers(Long userId, List<Long> roleIds) {
        log.info("Service: Removing roles {} from user ID: {}", roleIds, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Service: User not found for role removal, ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        Set<Role> roles = roleIds.stream()
                .map(rid -> roleRepository.findById(rid)
                        .orElseThrow(() -> {
                            log.error("Service: Role not found for removal, ID: {}", rid);
                            return new ResourceNotFoundException("Role not found with id: " + rid);
                        }))
                .collect(Collectors.toSet());
        user.getRoles().removeAll(roles);
        UserDTO dto = UserMapper.toDTO(userRepository.save(user));
        log.info("Service: Removed roles from user ID: {}", userId);
        return dto;
    }
}
