package com.banchango.domain.warehouses;

import com.banchango.domain.mainitemtypes.MainItemType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WarehousesRepository extends JpaRepository<Warehouses, Integer> {

    List<Warehouses> findAllByStatus(WarehouseStatus status, Pageable pageable);
    List<Warehouses> findByAddressContainingAndStatus(String address, WarehouseStatus status, Pageable pageable);
    Optional<Warehouses> findById(Integer warehouseId);
    <T> Optional<T> findById(Integer warehouseId, Class<T> type);
    <T> Optional<T> findByIdAndStatus(Integer warehouseId, WarehouseStatus status, Class<T> clazz);
    Optional<Warehouses> findByIdAndStatus(Integer warehouseId, WarehouseStatus status);
    List<Warehouses> findByUserId(Integer userId);

    @Query("select w from Warehouses w inner join MainItemTypes m on w.id=m.warehouse.id where m.type in :mainItemTypes and w.status='VIEWABLE' group by w.id having count(w.id)>0 order by count(w.id) desc, w.id desc")
    List<Warehouses> findViewableWarehouseByMainItemTypes(List<MainItemType> mainItemTypes, Pageable pageable);

    @Query("SELECT w FROM Warehouses w WHERE w.status = :status ORDER BY w.createdAt")
    List<Warehouses> findWarehousesByStatusOrderByCreatedAt(WarehouseStatus status, Pageable pageable);

    @Query("select w.status from Warehouses w where w.id=:warehouseId")
    WarehouseStatus findStatusById(Integer warehouseId);

    List<Warehouses> findByOrderByCreatedAtAsc(Pageable pageable);
}