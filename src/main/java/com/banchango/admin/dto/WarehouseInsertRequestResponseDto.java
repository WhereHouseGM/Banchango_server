package com.banchango.admin.dto;

import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.tools.DateConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseInsertRequestResponseDto {

    private Integer warehouseId;
    private String name;
    private String lastModifiedAt;
    private WarehouseStatus status;

    public WarehouseInsertRequestResponseDto(Warehouse warehouse) {
        this.warehouseId = warehouse.getId();
        this.name = warehouse.getName();
        this.lastModifiedAt = DateConverter.convertDateWithTime(warehouse.getLastModifiedAt());
        this.status = warehouse.getStatus();
    }
}
