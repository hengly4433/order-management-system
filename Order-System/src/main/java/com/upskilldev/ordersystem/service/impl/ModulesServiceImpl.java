package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.module.CreateModuleDTO;
import com.upskilldev.ordersystem.dto.module.ModuleDTO;
import com.upskilldev.ordersystem.dto.module.UpdateModuleDTO;
import com.upskilldev.ordersystem.entity.Modules;
import com.upskilldev.ordersystem.entity.Packages;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.ModuleMapper;
import com.upskilldev.ordersystem.repository.ModulesRepository;
import com.upskilldev.ordersystem.repository.PackageRepository;
import com.upskilldev.ordersystem.service.ModulesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModulesServiceImpl implements ModulesService {
    private final ModulesRepository modulesRepository;
    private final PackageRepository packageRepository;

    public ModulesServiceImpl(ModulesRepository modulesRepository, PackageRepository packageRepository) {
        this.modulesRepository = modulesRepository;
        this.packageRepository = packageRepository;
    }

    @Override
    public ModuleDTO createModule(CreateModuleDTO dto) {
        Packages pkg = packageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found " + dto.getPackageId()));
        Modules module = ModuleMapper.toEntity(dto, pkg);
        return ModuleMapper.toDTO(modulesRepository.save(module));
    }

    @Override
    public ModuleDTO updateModule(Long id, UpdateModuleDTO dto) {
        Modules module = modulesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found " + id));
        Packages pkg = packageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found " + dto.getPackageId()));
        ModuleMapper.updateEntity(dto, module, pkg);
        return ModuleMapper.toDTO(modulesRepository.save(module));
    }

    @Override
    public void deleteModule(Long id) {
        Modules module = modulesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found " + id));
        modulesRepository.delete(module);
    }

    @Override
    public ModuleDTO getModuleById(Long id) {
        return ModuleMapper.toDTO(modulesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found " + id)));
    }

    @Override
    public List<ModuleDTO> getAllModules() {
        return modulesRepository.findAll().stream().map(ModuleMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ModuleDTO> getModulesByPackageId(Long packageId) {
        return modulesRepository.findByPkg_Id(packageId).stream().map(ModuleMapper::toDTO).collect(Collectors.toList());
    }
}