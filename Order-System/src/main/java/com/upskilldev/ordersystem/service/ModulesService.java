package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.module.CreateModuleDTO;
import com.upskilldev.ordersystem.dto.module.ModuleDTO;
import com.upskilldev.ordersystem.dto.module.UpdateModuleDTO;

import java.util.List;

public interface ModulesService {
    ModuleDTO createModule(CreateModuleDTO dto);
    ModuleDTO updateModule(Long id, UpdateModuleDTO dto);
    void deleteModule(Long id);
    ModuleDTO getModuleById(Long id);
    List<ModuleDTO> getAllModules();
    List<ModuleDTO> getModulesByPackageId(Long packageId);
}
