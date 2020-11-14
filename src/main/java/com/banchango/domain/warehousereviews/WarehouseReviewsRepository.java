package com.banchango.domain.warehousereviews;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseReviewsRepository extends JpaRepository<WarehouseReviews, Integer> {

    List<WarehouseReviews> findByWarehouseId(Integer warehouseId, Pageable pageable);
    Optional<WarehouseReviews> findByReviewIdAndWarehouseId(Integer reviewId, Integer warehouseId);
    void deleteByReviewIdAndWarehouseId(Integer reviewId, Integer warehouseId);
    void deleteAllByWarehouseId(Integer warehouseId);
}
