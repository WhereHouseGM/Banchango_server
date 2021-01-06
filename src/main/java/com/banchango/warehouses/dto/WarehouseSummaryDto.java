package com.banchango.warehouses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WarehouseSummaryDto {
    private Integer warehouseId;
    private String name;
    private String address;

    @Builder
    public WarehouseSummaryDto(Integer warehouseId, String name, String address) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.address = address;
    }
}
