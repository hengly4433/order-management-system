package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto._package.CreatePackageDTO;
import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto._package.UpdatePackageDTO;
import com.upskilldev.ordersystem.entity.Packages;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.PackageMapper;
import com.upskilldev.ordersystem.repository.PackageRepository;
import com.upskilldev.ordersystem.service.PackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public PackageDTO createPackage(CreatePackageDTO dto) {
        Packages pkg = PackageMapper.toEntity(dto);
        return PackageMapper.toDTO(packageRepository.save(pkg));
    }

    @Override
    public PackageDTO updatePackage(Long id, UpdatePackageDTO dto) {
        Packages pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found " + id));
        PackageMapper.updateEntity(dto, pkg);
        return PackageMapper.toDTO(packageRepository.save(pkg));
    }

    @Override
    public void deletePackage(Long id) {
        Packages pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found " + id));
        packageRepository.delete(pkg);
    }

    @Override
    public PackageDTO getPackageById(Long id) {
        return PackageMapper.toDTO(packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found " + id)));
    }

    @Override
    public List<PackageDTO> getAllPackages() {
        return packageRepository.findAll().stream().map(PackageMapper::toDTO).collect(Collectors.toList());
    }
}