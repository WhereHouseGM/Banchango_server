package com.banchango.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class EstimateSummaryListResponseDto {
    private List<EstimateSummaryResponseDto> estimates;
}
