package com.upskilldev.ordersystem.repository;

import com.upskilldev.ordersystem.entity.Packages;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {
    Optional<Packages> findByName(String name);

    /**
     * Load each Package with its Modules and each Module with its Permissions,
     * ordering only by package name in the JPQL.
     * The eager fetching is driven by the EntityGraph.
     */
    @EntityGraph(attributePaths = {"modules", "modules.permissions"})
    @Query("SELECT p FROM Packages p ORDER BY p.name")
    List<Packages> findAllWithModulesAndPermissions();
}
