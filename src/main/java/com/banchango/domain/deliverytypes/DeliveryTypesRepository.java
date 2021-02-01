package com.banchango.domain.deliverytypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTypesRepository extends JpaRepository<DeliveryTypes, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<String> findByWarehouseId(Integer warehouseId);
}
