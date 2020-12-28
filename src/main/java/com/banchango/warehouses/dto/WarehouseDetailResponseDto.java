package com.banchango.warehouses.dto;

import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.MainItemType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WarehouseDetailResponseDto {
    private Integer warehouseId;
    private Integer ownerId;
    private String name;
    private Integer space;
    private String address;
    private String addressDetail;
    private String description;
    private Integer availableWeekdays;
    private String openAt;
    private String closeAt;
    private String availableTimeDetail;
    private String insurance;
    private Integer cctvExist;
    private String securityCompanyName;
    private Integer doorLockExist;
    private AirConditioningType airConditioningType;
    private Integer workerExist;
    private Integer canPickup;
    private Integer canPark;
    private MainItemType mainItemType;
    private WarehouseType warehouseType;
    private Integer minReleasePerMonth;
    private Double latitude;
    private Double longitude;

    private String mainImageUrl;

    private List<String> deliveryTypes = new ArrayList<>();
    private List<WarehouseCondition> warehouseCondition = new ArrayList<>();
    private List<String> warehouseFacilityUsages = new ArrayList<>();
    private List<String> warehouseUsageCautions = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    public WarehouseDetailResponseDto(Warehouses warehouse, String defaultImageUrl) {
        List<String> deliveryTypes = warehouse.getDeliveryTypes()
                .stream()
                .map(deliveryType -> deliveryType.getName())
                .collect(Collectors.toList());

        List<WarehouseCondition> warehouseConditionNames = warehouse.getWarehouseConditions()
                .stream()
                .map(condition -> condition.getCondition())
                .collect(Collectors.toList());

        List<String> warehouseFacilityUsages = warehouse.getWarehouseFacilityUsages()
                .stream()
                .map(facilityUsage -> facilityUsage.getContent())
                .collect(Collectors.toList());

        List<String> warehouseUsageCautions = warehouse.getWarehouseUsageCautions()
                .stream()
                .map(usageCaution -> usageCaution.getContent())
                .collect(Collectors.toList());

        List<String> images = warehouse.getWarehouseImages()
                .stream()
                .filter(image -> !image.isMain())
                .map(image -> image.getUrl())
                .collect(Collectors.toList());

        WarehouseImages mainImage = warehouse.getMainImage();

        this.warehouseId = warehouse.getId();
        this.ownerId = warehouse.getUserId();
        this.name = warehouse.getName();
        this.space = warehouse.getSpace();
        this.address = warehouse.getAddress();
        this.addressDetail = warehouse.getAddressDetail();
        this.description = warehouse.getDescription();
        this.availableWeekdays = warehouse.getAvailableWeekdays();
        this.openAt = warehouse.getOpenAt();
        this.closeAt = warehouse.getCloseAt();
        this.availableTimeDetail = warehouse.getAvailableTimeDetail();
        this.insurance = warehouse.getInsurance();
        this.cctvExist = warehouse.getCctvExist();
        this.securityCompanyName = warehouse.getSecurityCompanyName();
        this.doorLockExist = warehouse.getDoorLockExist();
        this.airConditioningType = warehouse.getAirConditioningType();
        this.workerExist = warehouse.getWorkerExist();
        this.canPickup = warehouse.getCanPickup();
        this.canPark = warehouse.getCanPark();
        this.mainItemType = warehouse.getMainItemType();
        this.warehouseType = warehouse.getWarehouseType();
        this.minReleasePerMonth = warehouse.getMinReleasePerMonth();
        this.latitude = warehouse.getLatitude();
        this.longitude = warehouse.getLongitude();

        this.mainImageUrl = mainImage != null ? mainImage.getUrl() : defaultImageUrl;

        this.deliveryTypes = deliveryTypes;
        this.warehouseCondition = warehouseConditionNames;
        this.warehouseFacilityUsages = warehouseFacilityUsages;
        this.warehouseUsageCautions = warehouseUsageCautions;
        this.images = images;
    }
}
