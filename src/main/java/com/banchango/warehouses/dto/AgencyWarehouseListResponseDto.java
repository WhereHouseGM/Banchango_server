package com.banchango.warehouses.dto;

import com.banchango.domain.agencywarehousedetails.AgencyWarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AgencyWarehouseListResponseDto {

    private String name;
    private Integer warehouseId;
    private String address;
    private String openAt;
    private String closeAt;
    private Integer minReleasePerMonth;
    private List<WarehouseTypesDto> warehouseConditions;
    private AgencyWarehouseType warehouseType;
    private String mainImageUrl;

    public AgencyWarehouseListResponseDto(Warehouses warehouse) {
        this.name = warehouse.getName();
        this.warehouseId = warehouse.getWarehouseId();
        this.address = warehouse.getAddress();
        this.openAt = warehouse.getOpenAt();
        this.closeAt = warehouse.getCloseAt();
    }

    private JSONArray toJSONArrayOfWarehouseConditions(List<WarehouseTypesDto> typesDtos) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        for(WarehouseTypesDto dto : typesDtos) {
            jsonArray.put(dto.getName());
        }
        return jsonArray;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("name", name);
        jsonObject.put("warehouseId", warehouseId);
        jsonObject.put("address", address);
        jsonObject.put("openAt", openAt);
        jsonObject.put("closeAt", closeAt);
        jsonObject.put("warehouseCondition", toJSONArrayOfWarehouseConditions(warehouseConditions));
        jsonObject.put("warehouseType", warehouseType);
        jsonObject.put("mainImageUrl", mainImageUrl);
        jsonObject.put("minReleasePerMonth", minReleasePerMonth);
        return jsonObject;
    }

}
