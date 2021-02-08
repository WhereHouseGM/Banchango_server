package com.banchango.domain.mainitemtypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainItemTypeRepository extends JpaRepository<MainItemType, Integer> {
    List<MainItemType> findByWarehouseId(Integer warehouseId);
    void deleteByWarehouseId(Integer warehouseId);
}
