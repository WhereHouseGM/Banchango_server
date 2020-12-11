package com.banchango.warehouses.dto;

import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseUsageCautionsResponseDto {

    private String content;
    private Integer warehouseId;

    public WarehouseUsageCautionsResponseDto(WarehouseUsageCautions cautions) {
        this.content = cautions.getContent();
        this.warehouseId = cautions.getWarehouseId();
    }
}
