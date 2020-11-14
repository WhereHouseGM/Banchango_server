package com.banchango.domain.warehouses;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehousesRepository extends JpaRepository<Warehouses, Integer> {

    List<Warehouses> findByAddressContaining(String address, Pageable pageable);
    Optional<Warehouses> findByUserId(Integer userId);
    Optional<Warehouses> findByWarehouseId(Integer warehouseId);
    void deleteByWarehouseId(Integer warehouseId);
}