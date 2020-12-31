package com.banchango.warehouses.dto;

import com.banchango.domain.mainitemtypes.MainItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseMainItemTypeMatchDto {
    private MainItemType name;
    private Boolean match;

    public WarehouseMainItemTypeMatchDto(MainItemType mainItemType, List<MainItemType> queriedMainItemTypes) {
        this.name = mainItemType;
        this.match = queriedMainItemTypes.contains(mainItemType);
    }
}
