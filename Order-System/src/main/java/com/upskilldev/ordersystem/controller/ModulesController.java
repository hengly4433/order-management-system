package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.module.CreateModuleDTO;
import com.upskilldev.ordersystem.dto.module.ModuleDTO;
import com.upskilldev.ordersystem.dto.module.UpdateModuleDTO;
import com.upskilldev.ordersystem.service.ModulesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@Tag(name = "Module Management")
public class ModulesController {

    private final ModulesService modulesService;

    public ModulesController(ModulesService modulesService) {
        this.modulesService = modulesService;
    }

    @PostMapping
    public ResponseEntity<ModuleDTO> create(@Valid @RequestBody CreateModuleDTO dto) {
        return ResponseEntity.ok(modulesService.createModule(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateModuleDTO dto) {
        return ResponseEntity.ok(modulesService.updateModule(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modulesService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(modulesService.getModuleById(id));
    }

    @GetMapping
    public ResponseEntity<List<ModuleDTO>> getAll() {
        return ResponseEntity.ok(modulesService.getAllModules());
    }

    @GetMapping("/by-package/{packageId}")
    public ResponseEntity<List<ModuleDTO>> getByPackage(@PathVariable Long packageId) {
        return ResponseEntity.ok(modulesService.getModulesByPackageId(packageId));
    }
}
