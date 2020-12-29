package com.banchango.domain.warehouses;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WarehousesRepository extends JpaRepository<Warehouses, Integer> {

    @Query(value = "DELETE FROM warehouses WHERE warehouse_id=?", nativeQuery = true)
    @Transactional
    @Modifying
    void delete_(Integer warehouseId);

    void deleteByMainItemType(MainItemType mainItemType);

    List<Warehouses> findAllByIsViewable(Boolean isViewable, Pageable pageable);
    List<Warehouses> findByAddressContainingAndIsViewable(String address, Boolean isViewable, Pageable pageable);
    Optional<Warehouses> findById(Integer warehouseId);
    List<Warehouses> findByUserId(Integer userId);
    List<Warehouses> findByMainItemTypeAndIsViewable(MainItemType mainItemType, Boolean isViewable, Pageable pageable);
}