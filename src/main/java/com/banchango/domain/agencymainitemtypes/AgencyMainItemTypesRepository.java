package com.banchango.domain.agencymainitemtypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgencyMainItemTypesRepository extends JpaRepository<AgencyMainItemTypes, Integer> {
    AgencyMainItemTypes findByAgencyWarehouseDetailId(Integer agencyWarehouseDetailId);

    @Query(value = "SELECT warehouse_id FROM agency_main_item_types amit NATURAL JOIN agency_warehouse_details awd WHERE amit.name=?", nativeQuery = true)
    List<Integer> getWarehouseIdsByWarehouseType(String type);
}
