//package com.banchango.warehouses.dto;
//
//import com.banchango.domain.warehouselocations.WarehouseLocations;
//import com.banchango.tools.ObjectMaker;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.json.JSONObject;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class WarehouseLocationDto {
//
//    private double latitude;
//    private double longitude;
//
//    public WarehouseLocationDto(WarehouseLocations location) {
//        this.latitude = location.getLatitude();
//        this.longitude = location.getLongitude();
//    }
//
//    public WarehouseLocations toEntity(Integer warehouseId) {
//        return WarehouseLocations.builder()
//                .latitude(latitude).longitude(longitude)
//                .warehouseId(warehouseId).build();
//    }
//
//    public JSONObject toJSONObject() {
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        jsonObject.put("latitude", latitude);
//        jsonObject.put("longitude", longitude);
//        return jsonObject;
//    }
//}