package com.banchango.domain.estimates;

import java.time.LocalDateTime;

public interface EstimateStatusAndLastModifiedAtAndWarehouseIdProjection {
    Integer getId();
    EstimateStatus getStatus();
    LocalDateTime getLastModifiedAt();
    Integer getWarehouseId();
}
