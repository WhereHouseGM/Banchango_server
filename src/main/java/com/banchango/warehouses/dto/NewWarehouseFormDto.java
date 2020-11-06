package com.banchango.warehouses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewWarehouseFormDto {

    private String name;
    private String serviceType;
    private String address;
    private String addressDetail;
    private String description;
    private int availableWeekdays;
    private String openAt;
    private String closeAt;
    private String availableTimeDetail;
    private boolean cctvExist;
    private boolean securityCompanyExist;
    private String securityCompanyName;
    private boolean doorLockExist;
    private String airConditioningType;
    private boolean workerExist;
    private boolean insuranceExist;
    private String insuranceName;
    private boolean canPickup;
    private boolean canPark;
    private int parkingScale;
    private int[] attachmentIds;
    // TODO : attachmentIds(array[integer])

    private NewLocationFormDto location;
    private int landArea;
    private int totalArea;
    private String types;
    private NewWarehouseDetailDto additionalInfo;
    // TODO : one of NewAgencyWarehousDetailDto, NewGeneralWarehouseDetailFormDto

}
