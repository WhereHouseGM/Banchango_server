package com.banchango.warehouses.dto;

import com.banchango.domain.warehouses.Warehouses;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseSearchResponseDto {

    private Integer warehouseId;
    private String name;
    private Integer landArea;
    private Integer totalArea;
    private Integer canUse;

    public WarehouseSearchResponseDto(Warehouses warehouse) {
        this.warehouseId = warehouse.getWarehouseId();
        this.name = warehouse.getName();
        this.landArea = warehouse.getLandArea();
        this.totalArea = warehouse.getTotalArea();
        this.canUse = warehouse.getCanUse();
    }

    private JSONObject makeBasicSearchResultObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("warehouseId", warehouseId);
        jsonObject.put("name", name);
        jsonObject.put("landArea", landArea);
        jsonObject.put("totalArea", totalArea);
        jsonObject.put("canUse", canUse);
        return jsonObject;
    }

    private JSONArray toJSONArrayOfWarehouseConditions(List<WarehouseTypesDto> typesDtos) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        for(WarehouseTypesDto dto : typesDtos) {
            jsonArray.put(dto.getName());
        }
        return jsonArray;
    }

    public JSONObject toJSONObject(WarehouseLocationDto locationDto, String mainImageUrl, List<WarehouseTypesDto> typesDtos) {
        JSONObject jsonObject = makeBasicSearchResultObject();
        jsonObject.put("mainImageUrl", mainImageUrl);
        JSONObject locationObject = ObjectMaker.getJSONObject();
        locationObject.put("latitude", locationDto.getLatitude());
        locationObject.put("longitude", locationDto.getLongitude());
        jsonObject.put("location", locationObject);
        jsonObject.put("warehouseCondition", toJSONArrayOfWarehouseConditions(typesDtos));
        return jsonObject;
    }
}