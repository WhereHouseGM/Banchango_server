package com.banchango.domain.warehousefacilityusages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseFacilityUsagesRepository extends JpaRepository<WarehouseFacilityUsages, Integer> {

    List<WarehouseFacilityUsages> findByWarehouseId(Integer warehouseId);

}
