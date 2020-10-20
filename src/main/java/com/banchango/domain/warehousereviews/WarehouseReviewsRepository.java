package com.banchango.domain.warehousereviews;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseReviewsRepository extends JpaRepository<WarehouseReviews, Integer> {

    List<WarehouseReviews> findByWarehouseId(Integer warehouseId, Pageable pageable);
    void deleteByIdAndWarehouseId(Integer reviewId, Integer warehouseId);
}
