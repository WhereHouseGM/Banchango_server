package com.banchango.warehouses.dto;

import com.banchango.domain.warehousetypes.WarehouseTypeName;
import com.banchango.domain.warehousetypes.WarehouseTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseTypesDto {

    private WarehouseTypeName name;

    public WarehouseTypesDto(WarehouseTypes warehouseTypes) {
        this.name =  warehouseTypes.getName();
    }
}
