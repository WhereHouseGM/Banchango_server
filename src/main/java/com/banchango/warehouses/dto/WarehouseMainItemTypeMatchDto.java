package com.banchango.warehouses.dto;

import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseMainItemTypeMatchDto {
    private MainItemType type;
    private Boolean match;

    public WarehouseMainItemTypeMatchDto(MainItemType mainItemType, List<MainItemType> queriedMainItemTypes) {
        this.type = mainItemType;
        this.match = queriedMainItemTypes.contains(mainItemType);
    }
}
