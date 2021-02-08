package com.banchango.domain.warehouses;

import com.banchango.domain.mainitemtypes.ItemType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehousesRepository extends JpaRepository<Warehouse, Integer> {

    List<Warehouse> findAllByStatus(WarehouseStatus status, Pageable pageable);
    List<Warehouse> findByAddressContainingAndStatus(String address, WarehouseStatus status, Pageable pageable);
    Optional<Warehouse> findById(Integer warehouseId);
    <T> Optional<T> findById(Integer warehouseId, Class<T> type);
    <T> Optional<T> findByIdAndStatus(Integer warehouseId, WarehouseStatus status, Class<T> clazz);
    Optional<Warehouse> findByIdAndStatus(Integer warehouseId, WarehouseStatus status);
    List<Warehouse> findByUserId(Integer userId);

    @Query("select w from Warehouse w inner join MainItemType m on w.id=m.warehouse.id where m.type in :mainItemTypes and w.status='VIEWABLE' group by w.id having count(w.id)>0 order by count(w.id) desc, w.id desc")
    List<Warehouse> findViewableWarehouseByMainItemTypes(List<ItemType> mainItemTypes, Pageable pageable);

    @Query("SELECT w FROM Warehouse w WHERE w.status = :status ORDER BY w.createdAt")
    List<Warehouse> findWarehousesByStatusOrderByCreatedAt(WarehouseStatus status, Pageable pageable);

    @Query("select w.status from Warehouse w where w.id=:warehouseId")
    WarehouseStatus findStatusById(Integer warehouseId);

    List<Warehouse> findByOrderByCreatedAtAsc(Pageable pageable);
}