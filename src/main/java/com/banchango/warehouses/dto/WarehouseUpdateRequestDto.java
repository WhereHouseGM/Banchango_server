package com.banchango.warehouses.dto;

import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseUpdateRequestDto extends WarehouseUpdateRequestParentDto{

    @Builder
    public WarehouseUpdateRequestDto(String name, Integer space, String address, String addressDetail, String description, Integer availableWeekdays, String openAt, String closeAt, String availableTimeDetail, Boolean cctvExist, Boolean doorLockExist, AirConditioningType airConditioningType, Boolean workerExist, Boolean canPark, List<ItemType> mainItemTypes, WarehouseType warehouseType, Integer minReleasePerMonth, Double latitude, Double longitude, List<String> deliveryTypes, List<WarehouseCondition> warehouseCondition, List<String> warehouseFacilityUsages, List<String> warehouseUsageCautions, List<String> insurances, List<String> securityCompanies) {
        this.name = name;
        this.space = space;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
        this.availableWeekdays = availableWeekdays;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.availableTimeDetail = availableTimeDetail;
        this.cctvExist = cctvExist;
        this.doorLockExist = doorLockExist;
        this.airConditioningType = airConditioningType;
        this.workerExist = workerExist;
        this.canPark = canPark;
        this.mainItemTypes = mainItemTypes;
        this.warehouseType = warehouseType;
        this.minReleasePerMonth = minReleasePerMonth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deliveryTypes = deliveryTypes;
        this.warehouseCondition = warehouseCondition;
        this.warehouseFacilityUsages = warehouseFacilityUsages;
        this.warehouseUsageCautions = warehouseUsageCautions;
        this.insurances = insurances;
        this.securityCompanies = securityCompanies;
    }
}
