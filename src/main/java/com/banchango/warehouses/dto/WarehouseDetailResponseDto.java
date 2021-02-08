package com.banchango.warehouses.dto;

import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.warehouseconditions.WarehouseConditionType;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsage;
import com.banchango.domain.warehouseimages.WarehouseImage;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
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
    private Boolean cctvExist;
    private Boolean doorLockExist;
    private AirConditioningType airConditioningType;
    private Boolean workerExist;
    private Boolean canPark;
    private List<ItemType> mainItemTypes;
    private WarehouseType warehouseType;
    private Integer minReleasePerMonth;
    private Double latitude;
    private Double longitude;
    private String blogUrl;

    private String mainImageUrl;

    private List<String> deliveryTypes = new ArrayList<>();
    private List<WarehouseConditionType> warehouseCondition = new ArrayList<>();
    private List<String> warehouseFacilityUsages = new ArrayList<>();
    private List<String> warehouseUsageCautions = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> insurances = new ArrayList<>();
    private List<String> securityCompanies = new ArrayList<>();

    public WarehouseDetailResponseDto(Warehouses warehouse, String defaultImageUrl) {
        List<ItemType> mainItemTypes = warehouse.getMainItemTypes()
            .stream()
            .map(MainItemType::getType)
            .collect(Collectors.toList());

        List<String> deliveryTypes = warehouse.getDeliveryTypes()
            .stream()
            .map(DeliveryType::getName)
            .collect(Collectors.toList());

        List<WarehouseConditionType> warehouseConditionNames = warehouse.getWarehouseConditions()
            .stream()
            .map(WarehouseCondition::getCondition)
            .collect(Collectors.toList());

        List<String> warehouseFacilityUsages = warehouse.getWarehouseFacilityUsages()
            .stream()
            .map(WarehouseFacilityUsage::getContent)
            .collect(Collectors.toList());

        List<String> warehouseUsageCautions = warehouse.getWarehouseUsageCautions()
            .stream()
            .map(WarehouseUsageCautions::getContent)
            .collect(Collectors.toList());

        List<String> images = warehouse.getWarehouseImages()
            .stream()
            .filter(image -> !image.isMain())
            .map(WarehouseImage::getUrl)
            .collect(Collectors.toList());

        List<String> insurances = warehouse.getInsurances()
                .stream()
                .map(Insurance::getName)
                .collect(Collectors.toList());

        List<String> securityCompanies = warehouse.getSecurityCompanies()
                .stream()
                .map(SecurityCompany::getName)
                .collect(Collectors.toList());

        WarehouseImage mainImage = warehouse.getMainImage();

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
        this.cctvExist = warehouse.getCctvExist();
        this.doorLockExist = warehouse.getDoorLockExist();
        this.airConditioningType = warehouse.getAirConditioningType();
        this.workerExist = warehouse.getWorkerExist();
        this.canPark = warehouse.getCanPark();
        this.mainItemTypes = mainItemTypes;
        this.warehouseType = warehouse.getWarehouseType();
        this.minReleasePerMonth = warehouse.getMinReleasePerMonth();
        this.latitude = warehouse.getLatitude();
        this.longitude = warehouse.getLongitude();
        this.blogUrl = warehouse.getBlogUrl();
        this.mainImageUrl = mainImage != null ? mainImage.getUrl() : defaultImageUrl;
        this.deliveryTypes = deliveryTypes;
        this.warehouseCondition = warehouseConditionNames;
        this.warehouseFacilityUsages = warehouseFacilityUsages;
        this.warehouseUsageCautions = warehouseUsageCautions;
        this.insurances = insurances;
        this.securityCompanies = securityCompanies;
        this.images = images;
    }
}
