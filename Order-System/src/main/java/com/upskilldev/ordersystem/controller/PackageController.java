package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto._package.CreatePackageDTO;
import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.dto._package.UpdatePackageDTO;
import com.upskilldev.ordersystem.service.PackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package Management")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    public ResponseEntity<PackageDTO> create(@Valid @RequestBody CreatePackageDTO dto) {
        return ResponseEntity.ok(packageService.createPackage(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageDTO> update(@PathVariable Long id, @Valid @RequestBody UpdatePackageDTO dto) {
        return ResponseEntity.ok(packageService.updatePackage(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.getPackageById(id));
    }

    @GetMapping
    public ResponseEntity<List<PackageDTO>> getAll() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }
}