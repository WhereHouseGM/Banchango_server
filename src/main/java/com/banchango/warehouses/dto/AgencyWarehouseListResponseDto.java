package com.banchango.warehouses.dto;

import com.banchango.domain.agencywarehousedetails.AgencyWarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehousetypes.WarehouseTypeName;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@NoArgsConstructor
@Getter
@Setter
public class AgencyWarehouseListResponseDto {

    // TODO : 사진 url 링크 추가
    private String name;
    private Integer warehouseId;
    private String address;
    private String openAt;
    private String closeAt;
    private WarehouseTypeName warehouseCondition;
    private AgencyWarehouseType warehouseType;

    public AgencyWarehouseListResponseDto(Warehouses warehouse) {
        this.name = warehouse.getName();
        this.warehouseId = warehouse.getWarehouseId();
        this.address = warehouse.getAddress();
        this.openAt = warehouse.getOpenAt();
        this.closeAt = warehouse.getCloseAt();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("name", name);
        jsonObject.put("warehouseId", warehouseId);
        jsonObject.put("address", address);
        jsonObject.put("openAt", openAt);
        jsonObject.put("closeAt", closeAt);
        jsonObject.put("warehouseCondition", warehouseCondition);
        jsonObject.put("warehouseType", warehouseType);
        return jsonObject;
    }

}
