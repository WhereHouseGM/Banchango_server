package com.banchango.domain.securitycompanies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityCompaniesRepository extends JpaRepository<SecurityCompanies, Integer> {
    void deleteByWarehouseId(Integer warehouseId);
    List<String> findByWarehouseId(Integer warehouseId);
}
