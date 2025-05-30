package com.upskilldev.ordersystem.mapper;


import com.upskilldev.ordersystem.dto._package.CreatePackageDTO;
import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto._package.UpdatePackageDTO;
import com.upskilldev.ordersystem.entity.Packages;

import java.util.stream.Collectors;

public class PackageMapper {
    public static PackageDTO toDTO(Packages pkg) {
        PackageDTO dto = new PackageDTO();
        dto.setId(pkg.getId());
        dto.setName(pkg.getName());
        dto.setDescription(pkg.getDescription());
//        if (pkg.getModules() != null) {
//            dto.setModules(pkg.getModules().stream().map(ModuleMapper::toDTO).collect(Collectors.toList()));
//        }
        return dto;
    }

    public static Packages toEntity(CreatePackageDTO dto) {
        Packages pkg = new Packages();
        pkg.setName(dto.getName());
        pkg.setDescription(dto.getDescription());
        return pkg;
    }

    public static void updateEntity(UpdatePackageDTO dto, Packages pkg) {
        pkg.setName(dto.getName());
        pkg.setDescription(dto.getDescription());
    }
}
