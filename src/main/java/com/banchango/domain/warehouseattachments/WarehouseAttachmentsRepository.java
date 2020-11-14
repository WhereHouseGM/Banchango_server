package com.banchango.domain.warehouseattachments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseAttachmentsRepository extends JpaRepository<WarehouseAttachments, Integer> {
    List<WarehouseAttachments> findByWarehouseId(Integer warehouseId);
    void deleteAllByWarehouseId(Integer warehouseId);
}
