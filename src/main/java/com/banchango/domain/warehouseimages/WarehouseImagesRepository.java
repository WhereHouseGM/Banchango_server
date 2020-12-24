package com.banchango.domain.warehouseimages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseImagesRepository extends JpaRepository<WarehouseImages, Long> {
    List<WarehouseImages> findByWarehouseId(Integer warehouseId);
    List<WarehouseImages> findByWarehouseIdAndIsMain(Integer warehouseId, Integer isMain);
    Optional<WarehouseImages> findByUrlContaining(String fileName);
    void deleteByUrlContaining(String fileName);
}
