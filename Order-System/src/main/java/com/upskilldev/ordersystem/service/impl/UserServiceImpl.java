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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

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
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        Set<Role> roles = user.getRoles().stream()
                        .map(rid -> roleRepository.findById(rid.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found")))
                                .collect(Collectors.toSet());
        user.setRoles(roles);
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (!user.getUsername().equals(updateUserDTO.getUsername()))
            throw new ResourceNotFoundException("Username is token: " + updateUserDTO.getUsername());
        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());
        Set<Role> roles = updateUserDTO.getRoles().stream()
                .map(rid -> roleRepository.findById(rid.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO assignRoleToUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Set<Role> roles = user.getRoles().stream()
                .map(rid -> roleRepository.findById(rid.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO removeRoleFromUsers(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User found with id: " + userId));
        Set<Role> roles = user.getRoles().stream()
                .map(rid -> roleRepository.findById(rid.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                .collect(Collectors.toSet());
        user.getRoles().removeAll(roles);
        return UserMapper.toDTO(userRepository.save(user));
    }
}
