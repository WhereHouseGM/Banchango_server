package com.banchango.domain.estimates;

import java.time.LocalDateTime;

public interface EstimateStatusAndCreatedAtAndWarehouseIdProjection {
    Integer getId();
    EstimateStatus getStatus();
    LocalDateTime getCreatedAt();
    Integer getWarehouseId();
}
