package com.banchango.domain.warehouseconditions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseConditionsRepository extends JpaRepository<WarehouseCondition, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<WarehouseCondition> findByWarehouseId(Integer warehouseId);
}
