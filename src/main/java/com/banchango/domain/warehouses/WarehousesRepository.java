package com.banchango.domain.warehouses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehousesRepository extends JpaRepository<Warehouses, Integer> {

    Optional<Warehouses> findById(Integer warehouseId);
}