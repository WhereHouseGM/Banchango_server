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

    @Column(name = "canuse", columnDefinition = "tinyint(1) DEFAULT 1")
    private Integer canUse;

    @Column(name = "servicetype")
    private ServiceType serviceType;

    @Column(name = "landarea")
    private Integer landArea;

    @Column(name = "totalarea")
    private Integer totalArea;

    @Column(length = 100)
    private String address;

    @Column(name = "addressdetail", length = 100)
    private String addressDetail;

    @Column(length = 400)
    private String description;

    @Column(name = "availableweekdays")
    private Integer availableWeekdays;

    @Column(name = "openat")
    @Temporal(TemporalType.TIME)
    private Date openAt;

    @Column(name = "closeat")
    @Temporal(TemporalType.TIME)
    private Date closeAt;

    @Column(name = "availabletimedetail", length = 100)
    private String availableTimeDetail;

    @Column(name = "cctvexist")
    private Integer cctvExist;

    @Column(name = "securitycompanyexist")
    private Integer securityCompanyExist;

    @Column(name = "securitycompanyname", length = 100)
    private String securityCompanyName;

    @Column(name = "doorlockexist")
    private Integer doorLockExist;

    @Column(name = "airconditioningtype", columnDefinition = "ENUM('HEATING', 'COOLING', 'NONE') DEFAULT 'NONE'")
    @Enumerated(EnumType.STRING)
    private AirConditioningType airConditioningType;

    @Column(name = "workerexist")
    private Integer workerExist;

    @Column(name = "insuranceexist")
    private Integer insuranceExist;

    @Column(name = "insurancename", length = 100)
    private String insuranceName;

    @Column(name = "canpickup")
    private Integer canPickup;

    @Column(name = "canpark")
    private Integer canPark;

    @Column(name = "parkingscale")
    private Integer parkingScale;

    @Column(name = "ownerid")
    private Integer ownerId;
}
