//package com.banchango.warehouses.dto;
//
//import com.banchango.domain.warehouses.AirConditioningType;
//import com.banchango.domain.warehouses.Warehouses;
//import com.banchango.tools.ObjectMaker;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.json.JSONObject;
//
//@NoArgsConstructor
//@Getter
//@Setter
//public class WarehouseResponseDto {
//
//    private Integer warehouseId;
//    private Integer canUse;
//    private String name;
//    private Integer landArea;
//    private Integer totalArea;
//    private Integer insuranceId;
//    private String address;
//    private String addressDetail;
//    private String description;
//    private Integer availableWeekdays;
//    private String openAt;
//    private String closeAt;
//    private String availableTimeDetail;
//    private Integer cctvExist;
//    private Integer securityCompanyExist;
//    private String securityCompanyName;
//    private Integer doorLockExist;
//    private AirConditioningType airConditioningType;
//    private Integer workerExist;
//    private Integer canPickup;
//    private Integer canPark;
//    private Integer parkingScale;
//    private Integer userId;
//
//    public WarehouseResponseDto(Warehouses warehouse) {
//        this.warehouseId = warehouse.getWarehouseId();
//        this.canUse = warehouse.getCanUse();
//        this.name = warehouse.getName();
//        this.landArea = warehouse.getLandArea();
//        this.totalArea = warehouse.getTotalArea();
//        this.address = warehouse.getAddress();
//        this.addressDetail = warehouse.getAddressDetail();
//        this.description = warehouse.getDescription();
//        this.availableWeekdays = warehouse.getAvailableWeekdays();
//        this.openAt = warehouse.getOpenAt();
//        this.closeAt = warehouse.getCloseAt();
//        this.availableTimeDetail = warehouse.getAvailableTimeDetail();
//        this.cctvExist = warehouse.getCctvExist();
//        this.securityCompanyExist = warehouse.getSecurityCompanyExist();
//        this.securityCompanyName = warehouse.getSecurityCompanyName();
//        this.doorLockExist = warehouse.getDoorLockExist();
//        this.airConditioningType = warehouse.getAirConditioningType();
//        this.workerExist = warehouse.getWorkerExist();
//        this.canPickup = warehouse.getCanPickup();
//        this.canPark = warehouse.getCanPark();
//        this.parkingScale = warehouse.getParkingScale();
//        this.userId = warehouse.getUserId();
//        this.insuranceId = warehouse.getInsuranceId();
//    }
//
//    public JSONObject toJSONObject() {
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        jsonObject.put("warehouseId", warehouseId);
//        jsonObject.put("canUse", canUse);
//        jsonObject.put("name", name);
//        jsonObject.put("landArea", landArea);
//        jsonObject.put("totalArea", totalArea);
//        jsonObject.put("address", address);
//        jsonObject.put("addressDetail", addressDetail);
//        jsonObject.put("description", description);
//        jsonObject.put("availableWeekdays", availableWeekdays);
//        jsonObject.put("openAt", openAt);
//        jsonObject.put("closeAt", closeAt);
//        jsonObject.put("availableTimeDetail", availableTimeDetail);
//        jsonObject.put("cctvExist", cctvExist);
//        jsonObject.put("securityCompanyExist", securityCompanyExist);
//        jsonObject.put("securityCompanyName", securityCompanyName);
//        jsonObject.put("doorLockExist", doorLockExist);
//        jsonObject.put("airConditioningType", airConditioningType);
//        jsonObject.put("workerExist", workerExist);
//        jsonObject.put("canPickup", canPickup);
//        jsonObject.put("canPark", canPark);
//        jsonObject.put("parkingScale", parkingScale);
//        jsonObject.put("ownerId", userId);
//        return jsonObject;
//    }
//}
