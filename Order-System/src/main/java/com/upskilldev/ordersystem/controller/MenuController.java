package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto._package.PackageDTO;
import com.upskilldev.ordersystem.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;
    public MenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PackageDTO>> menu(@PathVariable Long userId) {
        return ResponseEntity.ok(menuService.getVisibleMenuForUser(userId));
    }
}
