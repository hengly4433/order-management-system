package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.user.CreateUserDTO;
import com.upskilldev.ordersystem.dto.user.UpdateUserDTO;
import com.upskilldev.ordersystem.dto.user.UserDTO;
import com.upskilldev.ordersystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        return null;
    }

    @Override
    public UserDTO update(UpdateUserDTO updateUserDTO) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public UserDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDTO assignRoleToUser(Long userId, List<Long> roleIds) {
        return null;
    }

    @Override
    public UserDTO removeRoleFromUsers(Long userId, List<Long> roleIds) {
        return null;
    }
}
