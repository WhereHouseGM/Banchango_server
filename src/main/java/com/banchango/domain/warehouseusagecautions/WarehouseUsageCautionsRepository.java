package com.banchango.domain.warehouseusagecautions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseUsageCautionsRepository extends JpaRepository<WarehouseUsageCautions, Integer> {

    List<WarehouseUsageCautions> findByWarehouseId(Integer warehouseId);
}
