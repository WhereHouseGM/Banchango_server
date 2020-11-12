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

    private JSONObject makeBasicSearchResultObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("id", warehouseId);
        jsonObject.put("name", name);
        jsonObject.put("landArea", landArea);
        jsonObject.put("totalArea", totalArea);
        jsonObject.put("canUse", canUse);
        return jsonObject;
    }
    public JSONObject toJSONObjectWithLocationAndAttachmentAndType(WarehouseLocationDto locationDto, WarehouseAttachmentDto attachmentDto, WarehouseTypesDto typesDto) {
        JSONObject jsonObject = makeBasicSearchResultObject();
        jsonObject.put("thumbnailUrl", attachmentDto.getUrl());
        JSONObject locationObject = ObjectMaker.getJSONObject();
        locationObject.put("latitude", locationDto.getLatitude());
        locationObject.put("longitude", locationDto.getLongitude());
        jsonObject.put("location", locationObject);
        jsonObject.put("type", typesDto.getName());
        return jsonObject;
    }

    public JSONObject toJSONObjectWithLocationAndType(WarehouseLocationDto locationDto, WarehouseTypesDto typesDto) {
        JSONObject jsonObject = makeBasicSearchResultObject();
        JSONObject locationObject = ObjectMaker.getJSONObject();
        locationObject.put("latitude", locationDto.getLatitude());
        locationObject.put("longitude", locationDto.getLongitude());
        jsonObject.put("location", locationObject);
        jsonObject.put("type", typesDto.getName());
        jsonObject.put("thumbnailUrl", "null");
        return jsonObject;
    }
}