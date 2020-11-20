package com.banchango.domain.warehousemainimages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseMainImagesRepository extends JpaRepository<WarehouseMainImages, Integer> {
    Optional<WarehouseMainImages> findByWarehouseId(Integer warehouseId);
}
