package com.banchango.domain.warehouseimages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseImagesRepository extends JpaRepository<WarehouseImages, Long> {
    List<WarehouseImages> findByWarehouseIdAndIsMain(Integer warehouseId, Boolean isMain);
    Optional<WarehouseImages> findByWarehouseIdAndUrlContaining(Integer warehouseId, String fileName);
    void deleteByWarehouseIdAndUrlContaining(Integer warehouseId, String fileName);
}
