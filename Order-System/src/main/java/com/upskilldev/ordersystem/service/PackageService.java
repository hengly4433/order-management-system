package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto._package.CreatePackageDTO;
import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto._package.UpdatePackageDTO;

import java.util.List;

public interface PackageService {
    PackageDTO createPackage(CreatePackageDTO dto);
    PackageDTO updatePackage(Long id, UpdatePackageDTO dto);
    void deletePackage(Long id);
    PackageDTO getPackageById(Long id);
    List<PackageDTO> getAllPackages();
}
