package com.banchango.estimates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class EstimateInsertRequestDto {
    private Integer warehouseId;
    private String content;
    private Integer monthlyAverageRelease;
    private List<EstimateItemInsertRequestDto> estimateItems;

    @Builder
    public EstimateInsertRequestDto(Integer warehouseId, String content, Integer monthlyAverageRelease, List<EstimateItemInsertRequestDto> estimateItems) {
        this.warehouseId = warehouseId;
        this.content = content;
        this.monthlyAverageRelease = monthlyAverageRelease;
        this.estimateItems = estimateItems;
    }
}
