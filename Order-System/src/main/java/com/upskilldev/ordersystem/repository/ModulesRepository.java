package com.upskilldev.ordersystem.repository;

import com.upskilldev.ordersystem.entity.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModulesRepository extends JpaRepository<Modules, Long> {
    List<Modules> findByPkg_Id(Long packageId);
    Optional<Modules> findByName(String name);
}
