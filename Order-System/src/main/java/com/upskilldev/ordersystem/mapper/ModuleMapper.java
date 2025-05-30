package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.module.CreateModuleDTO;
import com.upskilldev.ordersystem.dto.module.ModuleDTO;
import com.upskilldev.ordersystem.dto.module.UpdateModuleDTO;
import com.upskilldev.ordersystem.entity.Modules;
import com.upskilldev.ordersystem.entity.Packages;

import java.util.stream.Collectors;

public class ModuleMapper {
    public static ModuleDTO toDTO(Modules module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setName(module.getName());
        dto.setDescription(module.getDescription());
        dto.setPackageId(module.getPkg().getId());
        dto.setPackageName(module.getPkg().getName());
//        dto.setPermissions(
//                module.getPermissions().stream()
//                        .map(PermissionMapper::toDTO)
//                        .collect(Collectors.toSet())
//        );
        dto.setCreatedAt(module.getCreatedAt());
        dto.setUpdatedAt(module.getUpdatedAt());
        return dto;
    }

    public static Modules toEntity(CreateModuleDTO dto, Packages pkg) {
        Modules module = new Modules();
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setPkg(pkg);
        return module;
    }

    public static void updateEntity(UpdateModuleDTO dto, Modules module, Packages pkg) {
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setPkg(pkg);
    }
}
