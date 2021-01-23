package com.banchango.domain.estimates;

import java.time.LocalDateTime;

public interface EstimateStatusAndCreatedAtProjection {
    EstimateStatus getStatus();
    LocalDateTime getCreatedAt();
}
