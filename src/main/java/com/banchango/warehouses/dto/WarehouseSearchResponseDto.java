package com.banchango.warehouses.dto;

import com.banchango.domain.warehouses.Warehouses;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseSearchResponseDto {

    private Integer warehouseId;
    private String name;
    private Integer landArea;
    private Integer totalArea;
    private boolean canUse;

    public WarehouseSearchResponseDto(Warehouses warehouse) {
        this.warehouseId = warehouse.getWarehouseId();
        this.name = warehouse.getName();
        this.landArea = warehouse.getLandArea();
        this.totalArea = warehouse.getTotalArea();
        this.canUse = warehouse.getCanUse().equals(1);
    }

    public HashMap<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", warehouseId);
        map.put("name", name);
        map.put("landArea", landArea);
        map.put("totalArea", totalArea);
        map.put("canUse", canUse);
        return map;
    }

    public JSONObject toJSONObject(WarehouseLocationDto locationDto, WarehouseAttachmentDto attachmentDto, WarehouseTypesDto typesDto) {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("id", warehouseId);
        jsonObject.put("name", name);
        jsonObject.put("thumbnailUrl", attachmentDto.getUrl());
        jsonObject.put("landArea", landArea);
        jsonObject.put("totalArea", totalArea);
        jsonObject.put("canUse", canUse);
        JSONObject locationObject = ObjectMaker.getJSONObject();
        locationObject.put("latitude", locationDto.getLatitude());
        locationObject.put("longitude", locationDto.getLongitude());
        jsonObject.put("location", locationObject);
        jsonObject.put("types", typesDto.getTypes());
        return jsonObject;
    }
}