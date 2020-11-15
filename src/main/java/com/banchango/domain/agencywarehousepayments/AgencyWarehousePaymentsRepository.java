package com.banchango.domain.agencywarehousepayments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyWarehousePaymentsRepository extends JpaRepository<AgencyWarehousePayments, Integer> {
    List<AgencyWarehousePayments> findByAgencyWarehouseDetailId(Integer agencyWarehouseDetailId);
}
