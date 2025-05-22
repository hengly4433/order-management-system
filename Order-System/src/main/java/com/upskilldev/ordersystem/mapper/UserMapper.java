package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.user.UserDTO;
import com.upskilldev.ordersystem.entity.User;

import java.util.HashSet;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toSet()));
        return dto;
    }
}
