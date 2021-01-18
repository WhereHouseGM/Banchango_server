package com.banchango.estimates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
public class EstimateInsertRequestDto {
    @NotNull
    private Integer warehouseId;
    private String content;
    @NotNull
    private Integer monthlyAverageRelease;
    @NotNull
    private List<EstimateItemInsertRequestDto> estimateItems;

    @Builder
    public EstimateInsertRequestDto(Integer warehouseId, String content, Integer monthlyAverageRelease, List<EstimateItemInsertRequestDto> estimateItems) {
        this.warehouseId = warehouseId;
        this.content = content;
        this.monthlyAverageRelease = monthlyAverageRelease;
        this.estimateItems = estimateItems;
    }
}
