package com.banchango.warehouses.dto;

import com.banchango.common.validator.ValueOfEnum;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
public abstract class WarehouseUpdateRequestParentDto {
    @Size(min = 1, message = "name의 최소 길이는 1입니다.")
    @NotNull(message = "name이 없습니다.")
    protected String name;

    @NotNull(message = "space가 없습니다.")
    protected Integer space;

    @Size(min = 1, message = "address의 최소 길이는 1입니다.")
    @NotNull(message = "address가 없습니다.")
    protected String address;

    @Size(min = 1, message = "addressDetail의 최소 길이는 1입니다.")
    @NotNull(message = "addressDetail이 없습니다.")
    protected String addressDetail;

    @Size(min = 1, message = "description의 최소 길이는 1입니다.")
    @NotNull(message = "description이 없습니다.")
    protected String description;

    @NotNull(message = "availableWeekdays가 없습니다.")
    protected Integer availableWeekdays;

    @Size(min = 1, message = "openAt의 최소 길이는 1입니다.")
    @NotNull(message = "openAt이 없습니다.")
    protected String openAt;

    @Size(min = 1, message = "closeAt의 최소 길이는 1입니다.")
    @NotNull(message = "closeAt이 없습니다.")
    protected String closeAt;

    @Size(min = 1, message = "availableTimeDetail의 최소 길이는 1입니다.")
    @NotNull(message = "availableTimeDetail이 없습니다.")
    protected String availableTimeDetail;

    @NotNull(message = "cctvExist이 없습니다.")
    protected Boolean cctvExist;

    @NotNull(message = "doorLockExist이 없습니다.")
    protected Boolean doorLockExist;

    @ValueOfEnum(enumClass = AirConditioningType.class)
    @NotNull(message = "airConditioningType이 없습니다.")
    protected AirConditioningType airConditioningType;

    @NotNull(message = "workerExist이 없습니다.")
    protected Boolean workerExist;

    @NotNull(message = "canPark이 없습니다.")
    protected Boolean canPark;

    @ValueOfEnum(enumClass = ItemType.class)
    @NotNull(message = "mainItemTypes가 없습니다.")
    protected List<ItemType> mainItemTypes;

    @ValueOfEnum(enumClass = WarehouseType.class)
    @NotNull(message = "warehouseType이 없습니다.")
    protected WarehouseType warehouseType;

    @NotNull(message = "minReleasePerMonth이 없습니다.")
    protected Integer minReleasePerMonth;

    @NotNull(message = "latitude가 없습니다.")
    protected Double latitude;

    @NotNull(message = "longitude가 없습니다.")
    protected Double longitude;

    @NotNull(message = "deliveryTypes가 없습니다.")
    protected List<String> deliveryTypes;

    @ValueOfEnum(enumClass = WarehouseCondition.class)
    @NotNull(message = "warehouseCondition이 없습니다.")
    protected List<WarehouseCondition> warehouseCondition;

    protected List<String> warehouseFacilityUsages;

    protected List<String> warehouseUsageCautions;

    @NotNull(message = "보험사 정보가 없습니다.")
    protected List<String> insurances;

    @NotNull(message = "경비 업체 정보가 없습니다.")
    protected List<String> securityCompanies;
}
