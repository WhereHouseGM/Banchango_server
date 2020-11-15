package com.banchango.domain.deliverytypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTypesRepository extends JpaRepository<DeliveryTypes, Integer> {
    List<DeliveryTypes> findByAgencyWarehouseDetailId(Integer agencyWarehouseDetailId);
}
