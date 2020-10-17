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
    private Integer id;

    @Column(name = "canUse", columnDefinition = "tinyint(1) DEFAULT 1")
    private Integer canUse;

    @Column(name = "serviceType")
    private ServiceType serviceType;

    @Column(name = "landArea")
    private Integer landArea;

    @Column(name = "totalArea")
    private Integer totalArea;

    @Column(length = 100)
    private String address;

    @Column(name = "addressDetail", length = 100)
    private String addressDetail;

    @Column(length = 400)
    private String description;

    @Column(name = "availableWeekdays")
    private Integer availableWeekdays;

    @Column(name = "openAt")
    @Temporal(TemporalType.TIME)
    private Date openAt;

    @Column(name = "closeAt")
    @Temporal(TemporalType.TIME)
    private Date closeAt;

    @Column(name = "availableTimeDetail", length = 100)
    private String availableTimeDetail;

    @Column(name = "cctvExist")
    private Integer cctvExist;

    @Column(name = "securityCompanyExist")
    private Integer securityCompanyExist;

    @Column(name = "securityCompanyName", length = 100)
    private String securityCompanyName;

    @Column(name = "doorLockExist")
    private Integer doorLockExist;

    @Column(name = "airConditioningType", columnDefinition = "ENUM('HEATING', 'COOLING', 'NONE') DEFAULT 'NONE'")
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column(name = "workerExist")
    private Integer workerExist;

    @Column(name = "insuranceExist")
    private Integer insuranceExist;

    @Column(name = "insuranceName", length = 100)
    private String insuranceName;

    @Column(name = "canPickup")
    private Integer canPickup;

    @Column(name = "canPark")
    private Integer canPark;

    @Column(name = "parkingScale")
    private Integer parkingScale;

    @Column(name = "ownerId")
    private Integer ownerId;
}
