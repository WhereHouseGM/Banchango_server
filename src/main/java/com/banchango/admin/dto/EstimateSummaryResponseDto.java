package com.banchango.admin.dto;

import com.banchango.domain.estimates.EstimateStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EstimateSummaryResponseDto {
    private EstimateStatus status;
    private String name;
    private String createdAt;
}
