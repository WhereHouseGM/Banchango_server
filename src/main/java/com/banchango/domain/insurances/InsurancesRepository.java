package com.banchango.domain.insurances;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsurancesRepository extends JpaRepository<Insurance, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<Insurance> findByWarehouseId(Integer warehouseId);
}
