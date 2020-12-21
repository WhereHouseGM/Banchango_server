package com.banchango.warehouses.dto;

import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseTypesDto {

    private WarehouseCondition name;

    public WarehouseTypesDto(WarehouseConditions warehouseConditions) {
        this.name =  warehouseConditions.getName();
    }
}
