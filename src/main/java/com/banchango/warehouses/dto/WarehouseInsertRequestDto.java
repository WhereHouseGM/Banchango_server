package com.banchango.warehouses.dto;

import com.banchango.common.validator.ValueOfEnum;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.MainItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
public class WarehouseInsertRequestDto {

    @Size(min = 1, message = "name의 최소 길이는 1입니다.")
    @NotNull(message = "name이 없습니다.")
    String name;

    @NotNull(message = "space가 없습니다.")
    Integer space;

    @Size(min = 1, message = "address의 최소 길이는 1입니다.")
    @NotNull(message = "address가 없습니다.")
    String address;

    @Size(min = 1, message = "addressDetail의 최소 길이는 1입니다.")
    @NotNull(message = "addressDetail이 없습니다.")
    String addressDetail;

    @Size(min = 1, message = "description의 최소 길이는 1입니다.")
    @NotNull(message = "description이 없습니다.")
    String description;

    @NotNull(message = "availableWeekdays가 없습니다.")
    Integer availableWeekdays;

    @Size(min = 1, message = "openAt의 최소 길이는 1입니다.")
    @NotNull(message = "openAt이 없습니다.")
    String openAt;

    @Size(min = 1, message = "closeAt의 최소 길이는 1입니다.")
    @NotNull(message = "closeAt이 없습니다.")
    String closeAt;

    @Size(min = 1, message = "availableTimeDetail의 최소 길이는 1입니다.")
    @NotNull(message = "availableTimeDetail이 없습니다.")
    String availableTimeDetail;

    @Size(min = 1, message = "insurance의 최소 길이는 1입니다.")
    @NotNull(message = "insurance이 없습니다.")
    String insurance;

    @NotNull(message = "cctvExist이 없습니다.")
    Boolean cctvExist;

    String securityCompanyName;

    @NotNull(message = "doorLockExist이 없습니다.")
    Boolean doorLockExist;

    @ValueOfEnum(enumClass = AirConditioningType.class)
    @NotNull(message = "airConditioningType이 없습니다.")
    AirConditioningType airConditioningType;

    @NotNull(message = "workerExist이 없습니다.")
    Boolean workerExist;

    @NotNull(message = "canPickup이 없습니다.")
    Boolean canPickup;

    @NotNull(message = "canPark이 없습니다.")
    Boolean canPark;

    @ValueOfEnum(enumClass = MainItemType.class)
    @NotNull(message = "mainItemType이 없습니다.")
    MainItemType mainItemType;

    @ValueOfEnum(enumClass = WarehouseType.class)
    @NotNull(message = "warehouseType이 없습니다.")
    WarehouseType warehouseType;

    @NotNull(message = "minReleasePerMonth이 없습니다.")
    Integer minReleasePerMonth;

    @NotNull(message = "latitude가 없습니다.")
    Double latitude;

    @NotNull(message = "longitude가 없습니다.")
    Double longitude;

    @NotNull(message = "deliveryTypes가 없습니다.")
    List<String> deliveryTypes;

    @ValueOfEnum(enumClass = WarehouseCondition.class)
    @NotNull(message = "warehouseCondition이 없습니다.")
    List<WarehouseCondition> warehouseCondition;

    @NotNull(message = "warehouseFacilityUsages가 없습니다.")
    List<String> warehouseFacilityUsages;

    @NotNull(message = "warehouseUsageCautions가 없습니다.")
    List<String> warehouseUsageCautions;
}
