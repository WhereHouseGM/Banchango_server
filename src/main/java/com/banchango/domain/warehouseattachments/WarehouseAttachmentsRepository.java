package com.banchango.domain.warehouseattachments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseAttachmentsRepository extends JpaRepository<WarehouseAttachments, Integer> {
    List<WarehouseAttachments> findByWarehouseId(Integer warehouseId);
    void deleteByUrlContaining(String fileName);
    Optional<WarehouseAttachments> findByUrlContaining(String filaName);
}
