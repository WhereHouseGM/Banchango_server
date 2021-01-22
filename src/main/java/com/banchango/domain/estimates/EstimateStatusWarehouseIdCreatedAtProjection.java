package com.banchango.domain.estimates;

import java.time.LocalDateTime;

public interface EstimateStatusWarehouseIdCreatedAtProjection {
    EstimateStatus getStatus();
    Integer getWarehouseId();
    LocalDateTime getCreatedAt();
}
