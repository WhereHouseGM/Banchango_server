package com.banchango.domain.warehouses;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    // TODO : openAt, closeAt 타입 확인 필요
    @Column
    @Temporal(TemporalType.TIME)
    private Date openAt;

    @Column
    @Temporal(TemporalType.TIME)
    private Date closeAt;

    @Column(length = 100)
    private String availableTimeDetail;

    @Column(name = "cctvexist")
    private Integer cctvExist;

    @Column
    private Integer securityCompanyExist;

    @Column(length = 100)
    private String securityCompanyName;

    @Column(name = "doorlockexist")
    private Integer doorLockExist;

    @Column(columnDefinition = "ENUM('HEATING', 'COOLING', 'NONE') DEFAULT 'NONE'")
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column
    private Integer workerExist;

    @Column
    private Integer insuranceExist;

    @Column(length = 100)
    private String insuranceName;

    @Column
    private Integer canPickup;

    @Column
    private Integer canPark;

    @Column
    private Integer parkingScale;

    @Column
    private Integer userId;
}
