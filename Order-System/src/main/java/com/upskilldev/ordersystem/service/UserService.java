package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.user.CreateUserDTO;
import com.upskilldev.ordersystem.dto.user.UpdateUserDTO;
import com.upskilldev.ordersystem.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO);
    void deleteUser(Long userId);
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();

    UserDTO assignRoleToUser(Long userId, List<Long> roleIds);
    UserDTO removeRoleFromUsers(Long userId, List<Long> roleIds);
}
