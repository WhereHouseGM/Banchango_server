package com.banchango.factory.request;

import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.warehouseconditions.WarehouseConditionType;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;

import java.util.ArrayList;
import java.util.List;

public class WarehouseInsertRequestFactory {
    private static final String NAME = "NAME";
    private static final Integer SPACE = 3;
    private static final String ADDRESS = "ADDRESS";
    private static final String ADDRESS_DETAIL = "ADDRESS_DETAIL";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final Integer AVAILABLE_WEEKDAYS = 101011;
    private static final String OPEN_AT = "09:00";
    private static final String CLOSE_AT = "10:00";
    private static final String AVAILABLE_TIME_DETAIL = "AVAILABLE_TIME_DETAIL";
    private static final Boolean CCTV_EXISTS = true;
    private static final Boolean DOOR_LOCK_EXISTS = true;
    private static final AirConditioningType AIR_CONDITIONING_TYPE = AirConditioningType.BOTH;
    private static final Boolean WORKER_EXIST = true;
    private static final Boolean CAN_PARK = true;
    private static final List<ItemType> MAIN_ITEM_TYPES = new ArrayList<>();
    private static final WarehouseType WAREHOUSE_TYPE = WarehouseType.THREEPL;
    private static final Integer MIN_RELEASE_PER_MONTH = 1;
    private static final Double LATITUDE = 22.2;
    private static final Double LONGITUDE = 33.3;
    private static final List<String> DELIVERY_TYPES = new ArrayList<>();
    private static final List<WarehouseConditionType> WAREHOUSE_CONDITION = new ArrayList<>();
    private static final List<String> WAREHOUSE_FACILITY_USAGES = new ArrayList<>();
    private static final List<String> INSURANCES = new ArrayList<>();
    private static final List<String> SECURITY_COMPANIES = new ArrayList<>();

    public static WarehouseInsertRequestDto create() {
        return WarehouseInsertRequestDto.builder()
                .name(NAME)
                .space(SPACE)
                .address(ADDRESS)
                .addressDetail(ADDRESS_DETAIL)
                .description(DESCRIPTION)
                .availableWeekdays(AVAILABLE_WEEKDAYS)
                .openAt(OPEN_AT)
                .closeAt(CLOSE_AT)
                .availableTimeDetail(AVAILABLE_TIME_DETAIL)
                .cctvExist(CCTV_EXISTS)
                .doorLockExist(DOOR_LOCK_EXISTS)
                .airConditioningType(AIR_CONDITIONING_TYPE)
                .workerExist(WORKER_EXIST)
                .canPark(CAN_PARK)
                .mainItemTypes(MAIN_ITEM_TYPES)
                .warehouseType(WAREHOUSE_TYPE)
                .minReleasePerMonth(MIN_RELEASE_PER_MONTH)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .deliveryTypes(DELIVERY_TYPES)
                .warehouseCondition(WAREHOUSE_CONDITION)
                .warehouseFacilityUsages(WAREHOUSE_FACILITY_USAGES)
                .insurances(INSURANCES)
                .securityCompanies(SECURITY_COMPANIES)
                .build();
    }
}
