package com.banchango.domain.agencywarehousedetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyWarehouseDetailsRepository extends JpaRepository<AgencyWarehouseDetails, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    Optional<AgencyWarehouseDetails> findByWarehouseId(Integer warehouseId);
}
