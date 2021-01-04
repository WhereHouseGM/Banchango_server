package com.banchango.domain.warehouses;

import com.banchango.domain.mainitemtypes.MainItemType;
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

    List<Warehouses> findAllByStatus(WarehouseStatus status, Pageable pageable);
    List<Warehouses> findByAddressContainingAndStatus(String address, WarehouseStatus status, Pageable pageable);
    Optional<Warehouses> findById(Integer warehouseId);
    Optional<Warehouses> findByIdAndStatus(Integer warehouseId, WarehouseStatus status);
    List<Warehouses> findByUserId(Integer userId);

    @Query("select w from Warehouses w inner join MainItemTypes m on w.id=m.warehouse.id where m.type in :mainItemTypes and w.status='VIEWABLE' group by w.id having count(w.id)>0 order by count(w.id) desc")
    List<Warehouses> findViewableWarehouseByMainItemTypes(List<MainItemType> mainItemTypes, Pageable pageable);

    @Query("SELECT w FROM Warehouses w WHERE w.status = :status ORDER BY w.createdAt")
    List<Warehouses> findWarehousesByStatusOrderByCreatedAt(WarehouseStatus status, Pageable pageable);
}