package com.banchango.domain.warehouses;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Warehouses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warehouseId;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1")
    private Integer canUse;

    @Column(length = 20)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column
    private Integer landArea;

    @Column
    private Integer totalArea;

    @Column(length = 100)
    private String address;

    @Column(length = 100)
    private String addressDetail;

    @Column(length = 400)
    private String description;

    @Column
    private Integer availableWeekdays;

    @Column
    private String openAt;

    @Column
    private String closeAt;

    @Column(length = 100)
    private String availableTimeDetail;

    @Column
    private Integer insuranceId;

    @Column
    private Integer cctvExist;

    @Column
    private Integer securityCompanyExist;

    @Column(length = 100)
    private String securityCompanyName;

    @Column
    private Integer doorLockExist;

    @Column(columnDefinition = "ENUM('HEATING', 'COOLING', 'NONE') DEFAULT 'NONE'")
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column
    private Integer workerExist;

    @Column
    private Integer canPickup;

    @Column
    private Integer canPark;

    @Column
    private Integer parkingScale;

    @Column
    private Integer userId;

    @Builder
    public Warehouses(Integer canUse, String name, Integer insuranceId, String serviceType, Integer landArea, Integer totalArea, String address, String addressDetail, String description, Integer availableWeekdays, String openAt, String closeAt, String availableTimeDetail, Integer cctvExist, Integer securityCompanyExist, String securityCompanyName, Integer doorLockExist, String airConditioningType, Integer workerExist, Integer canPickup, Integer canPark, Integer parkingScale, Integer userId) {
        this.canUse = canUse;
        this.name = name;
        this.insuranceId = insuranceId;
        this.serviceType = ServiceType.valueOf(serviceType);
        this.landArea = landArea;
        this.totalArea = totalArea;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
        this.availableWeekdays = availableWeekdays;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.availableTimeDetail = availableTimeDetail;
        this.cctvExist = cctvExist;
        this.securityCompanyExist = securityCompanyExist;
        this.securityCompanyName = securityCompanyName;
        this.doorLockExist = doorLockExist;
        this.airConditioningType = AirConditioningType.valueOf(airConditioningType);
        this.workerExist = workerExist;
        this.canPickup = canPickup;
        this.canPark = canPark;
        this.parkingScale = parkingScale;
        this.userId = userId;
    }
}