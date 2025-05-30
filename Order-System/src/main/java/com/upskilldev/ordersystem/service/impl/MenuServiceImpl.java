package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto.module.ModuleDTO;
import com.upskilldev.ordersystem.entity.Modules;
import com.upskilldev.ordersystem.entity.Packages;
import com.upskilldev.ordersystem.entity.Permission;
import com.upskilldev.ordersystem.entity.User;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.ModuleMapper;
import com.upskilldev.ordersystem.mapper.PackageMapper;
import com.upskilldev.ordersystem.mapper.PermissionMapper;
import com.upskilldev.ordersystem.repository.UserRepository;
import com.upskilldev.ordersystem.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);
    private final UserRepository userRepository;

    public MenuServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<PackageDTO> getVisibleMenuForUser(Long userId) {
        log.info("Building menu for user {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));

        // collect user’s permissions
        Set<Permission> perms = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .collect(Collectors.toSet());

        // group by module → permissions
        Map<Modules, List<Permission>> modPerms = perms.stream()
                .collect(Collectors.groupingBy(Permission::getModule));

        // group modules by package
        Map<Packages, List<ModuleDTO>> pkgMap = new LinkedHashMap<>();
        modPerms.forEach((mod, pList) -> {
            ModuleDTO mDto = ModuleMapper.toDTO(mod);
            mDto.setPermissions(pList.stream().map(PermissionMapper::toDTO).collect(Collectors.toSet()));
            pkgMap.computeIfAbsent(mod.getPkg(), k -> new ArrayList<>()).add(mDto);
        });

        // build final list
        return pkgMap.entrySet().stream().map(e -> {
            PackageDTO pDto = PackageMapper.toDTO(e.getKey());
            pDto.setModules(e.getValue());
            return pDto;
        }).collect(Collectors.toList());
    }
}
