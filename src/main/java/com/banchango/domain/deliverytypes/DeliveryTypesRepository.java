package com.banchango.domain.deliverytypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTypesRepository extends JpaRepository<DeliveryType, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<DeliveryType> findByWarehouseId(Integer warehouseId);
}
