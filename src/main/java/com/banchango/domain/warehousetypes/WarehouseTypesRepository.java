package com.banchango.domain.warehousetypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseTypesRepository extends JpaRepository<WarehouseTypes, Integer> {

    WarehouseTypes findByWarehouseId(Integer warehouseId);
}
