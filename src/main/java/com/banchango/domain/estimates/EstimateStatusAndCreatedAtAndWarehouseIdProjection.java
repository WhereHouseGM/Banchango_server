package com.banchango.domain.estimates;

import java.time.LocalDateTime;

public interface EstimateStatusAndCreatedAtAndWarehouseIdProjection {
    EstimateStatus getStatus();
    LocalDateTime getCreatedAt();
    Integer getWarehouseId();
}
