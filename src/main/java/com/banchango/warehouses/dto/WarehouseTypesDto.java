package com.banchango.warehouses.dto;

import com.banchango.domain.warehousetypes.WarehouseTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WarehouseTypesDto {

    private String[] types;

    public WarehouseTypesDto(List<WarehouseTypes> warehouseTypes) {
        types = new String[10];
        for(int i = 0; i < warehouseTypes.size(); i++) {
            types[i] = warehouseTypes.get(i).toString();
        }
    }
}
