package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto._package.PackageDTO;

import java.util.List;

public interface MenuService {
    List<PackageDTO> getVisibleMenuForUser(Long userId);
}
