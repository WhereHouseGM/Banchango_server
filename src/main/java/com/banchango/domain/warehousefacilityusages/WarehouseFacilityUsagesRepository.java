package com.banchango.domain.warehousefacilityusages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseFacilityUsagesRepository extends JpaRepository<WarehouseFacilityUsage, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<WarehouseFacilityUsage> findByWarehouseId(Integer warehouseId);
}
