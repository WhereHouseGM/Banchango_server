package com.banchango.estimates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseEstimateInsertRequestDto {
    private Integer warehouseId;
    private String content;
    private List<WarehouseEstimateItemInsertRequestDto> estimateItems;

    @Builder
    public WarehouseEstimateInsertRequestDto(Integer warehouseId, String content, List<WarehouseEstimateItemInsertRequestDto> estimateItems) {
        this.warehouseId = warehouseId;
        this.content = content;
        this.estimateItems = estimateItems;
    }
}
