package com.banchango.domain.warehouseusagecautions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseUsageCautionsRepository extends JpaRepository<WarehouseUsageCaution, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<WarehouseUsageCaution> findByWarehouseId(Integer warehouseId);
}
