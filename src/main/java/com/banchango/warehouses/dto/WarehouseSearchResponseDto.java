package com.banchango.warehouses.dto;

import com.banchango.domain.warehouses.Warehouses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseSearchResponseDto {

    private Integer warehouseId;
    private String name;
    private String thumbnailUrl;
    private Integer landArea;
    private Integer totalArea;
    private boolean canUse;

    public WarehouseSearchResponseDto(Warehouses warehouse) {
        this.warehouseId = warehouse.getWarehouseId();
        this.name = warehouse.getName();
        // TODO : thumbnailUrl 처리 부분
        this.thumbnailUrl = null;
        this.landArea = warehouse.getLandArea();
        this.totalArea = warehouse.getTotalArea();
        this.canUse = warehouse.getCanUse().equals(1);
    }

    public HashMap<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", warehouseId);
        map.put("name", name);
        map.put("thumbnailUrl", thumbnailUrl);
        map.put("landArea", landArea);
        map.put("totalArea", totalArea);
        map.put("canUse", canUse);
        return map;
    }
}
