package com.banchango.domain.warehouses;

import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehousereviews.WarehouseReviews;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Warehouses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warehouseId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer space;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String addressDetail;

    @Column(length = 400, nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer availableWeekdays;

    @Column(nullable = false)
    private String openAt;

    @Column(nullable = false)
    private String closeAt;

    @Column(length = 100)
    private String availableTimeDetail;

    @Column
    private String insurance;

    @Column(nullable = false)
    private Integer cctvExist;

    @Column(length = 100)
    private String securityCompanyName;

    @Column(nullable = false)
    private Integer doorLockExist;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column(nullable = false)
    private Integer workerExist;

    @Column(nullable = false)
    private Integer canPickup;

    @Column(nullable = false)
    private Integer canPark;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemTypeName mainItemType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgencyWarehouseType warehouseType;

    @Column(nullable = false)
    private Integer minReleasePerMonth;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private Integer userId;

    @OneToMany
    private List<DeliveryTypes> deliveryTypes = new ArrayList<>();

    @OneToMany
    private List<WarehouseConditions> warehouseConditions = new ArrayList<>();

    @OneToMany
    private List<WarehouseFacilityUsages> warehouseFacilityUsages = new ArrayList<>();

    @OneToMany
    private List<WarehouseUsageCautions> warehouseUsageCautions = new ArrayList<>();

    @Builder
    public Warehouses(String name, String insurance, Integer space, String address, String addressDetail, String description, Integer availableWeekdays, String openAt, String closeAt, String availableTimeDetail, Integer cctvExist, String securityCompanyName, Integer doorLockExist, String airConditioningType, Integer workerExist, Integer canPickup, Integer canPark, ItemTypeName mainItemType, Integer userId, Double latitude, Double longitude) {
        this.name = name;
        this.insurance = insurance;
        this.space = space;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
        this.availableWeekdays = availableWeekdays;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.availableTimeDetail = availableTimeDetail;
        this.cctvExist = cctvExist;
        this.securityCompanyName = securityCompanyName;
        this.doorLockExist = doorLockExist;
        this.airConditioningType = AirConditioningType.valueOf(airConditioningType);
        this.workerExist = workerExist;
        this.canPickup = canPickup;
        this.canPark = canPark;
        this.mainItemType = mainItemType;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}