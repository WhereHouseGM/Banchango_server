package com.banchango.warehouses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgencyWarehouseInsertRequestDto extends WarehouseInsertRequestDto {

    private Integer canUse;
    private String name;
    private String serviceType;
    private Integer landArea;
    private Integer totalArea;
    private String address;
    private String addressDetail;
    private String description;
    private Integer availableWeekdays;
    private String openAt;
    private String closeAt;
    private String availableTimeDetail;
    private Integer cctvExist;
    private Integer securityCompanyExist;
    private String securityCompanyName;
    private Integer doorLockExist;
    private String airConditioningType;
    private Integer workerExist;
    private Integer canPickup;
    private Integer canPark;
    private Integer parkingScale;
    private InsuranceInsertRequestDto insurance;
    private String[] warehouseCondition;
    private WarehouseLocationDto location;
    private AgencyWarehouseDetailInsertRequestDto agencyDetails;
}
