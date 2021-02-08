package com.banchango.warehouses.dto;

import com.banchango.domain.mainitemtypes.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseMainItemTypeMatchDto {
    private ItemType name;
    private Boolean match;

    public WarehouseMainItemTypeMatchDto(ItemType mainItemType, List<ItemType> queriedMainItemTypes) {
        this.name = mainItemType;
        this.match = queriedMainItemTypes.contains(mainItemType);
    }
}
