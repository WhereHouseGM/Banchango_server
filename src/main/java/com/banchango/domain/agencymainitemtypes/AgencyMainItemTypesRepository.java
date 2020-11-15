package com.banchango.domain.agencymainitemtypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyMainItemTypesRepository extends JpaRepository<AgencyMainItemTypes, Integer> {
    AgencyMainItemTypes findByAgencyWarehouseDetailId(Integer agencyWarehouseDetailId);
}
