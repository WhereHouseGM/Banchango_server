package com.banchango.domain.warehousetypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseTypesRepository extends JpaRepository<WarehouseTypes, Integer> {

    WarehouseTypes findByWarehouseId(Integer warehouseId);
}
