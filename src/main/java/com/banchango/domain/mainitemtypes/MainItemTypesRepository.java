package com.banchango.domain.mainitemtypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainItemTypesRepository extends JpaRepository<MainItemTypes, Integer> {
    List<MainItemTypes> findByWarehouseId(Integer warehouseId);
    void deleteByWarehouseId(Integer warehouseId);
}
