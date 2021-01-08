package com.banchango.estimates.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstimateSearchResponseDto {
    private List<EstimateSearchDto> estimates;
}
