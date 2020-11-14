package com.banchango.domain.deliverytypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTypesRepository extends JpaRepository<DeliveryTypes, Integer> {
    void deleteAllByAgencyWarehouseDetailId(Integer agencyWarehouseDetailId);
}
