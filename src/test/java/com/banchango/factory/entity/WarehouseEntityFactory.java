package com.banchango.factory.entity;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouses.*;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehouseEntityFactory {
    public static final String NAME = "NAME";
    public static final Integer SPACE = 123;
    public static final String ADDRESS = "ADDRESS";
    public static final String ADDRESS_DETAIL = "ADDRESS_DETAIL";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final Integer AVAILABLE_WEEKDAYS = 1;
    public static final String OPEN_AT = "09:00";
    public static final String CLOSE_AT = "18:00";
    public static final String AVAILABLE_TIME_DETAIL = "AVAILABLE_TIME_DETAIL";
    public static final Boolean CCTV_EXISTS = true;
    public static final Boolean DOOR_LOCK_EXIST = true;
    public static final AirConditioningType AIR_CONDITIONING_TYPE = AirConditioningType.BOTH;
    public static final Boolean WORKER_EXIST = true;
    public static final Boolean CAN_PARK = true;
    public static final WarehouseType WAREHOUSE_TYPE = WarehouseType.THREEPL;
    public static final Integer MIN_RELEASE_PER_MONTH = 1000;
    public static final Double LATITUDE = 22.2;
    public static final Double LONGITUDE = 22.2;
    public static final String[] INSURANCES = {"INSURANCE_1", "INSURANCE_2", "INSURANCE_3"};
    public static final String[] SECURITY_COMPANIES = {"SEC_COMP_1", "SEC_COMP_2", "SEC_COMP_3"};
    public static final String[] DELIVERY_TYPES = {"DELIVERY_1", "DELIVERY_2", "DELIVERY_3"};
    public static final WarehouseCondition[] WAREHOUSE_CONDITIONS = {WarehouseCondition.ROOM_TEMPERATURE, WarehouseCondition.LOW_TEMPERATURE};
    public static final String[] WAREHOUSE_FACILITY_USAGES = {"FACILITY_1"};
    public static final String[] WAREHOUSE_USAGE_CAUTIONS = {"CAUTION_1", "CAUTION_2", "CAUTION_3"};

    public static final String NEW_NAME = "NEW_NAME";
    public static final Integer NEW_SPACE = 12345;
    public static final String NEW_ADDRESS = "NEW_ADDRESS";
    public static final String NEW_ADDRESS_DETAIL = "NEW_ADDRESS_DETAIL";
    public static final String NEW_DESCRIPTION = "NEW_DESCRIPTION";
    public static final Integer NEW_AVAILABLE_WEEKDAYS = 1001;
    public static final String NEW_OPEN_AT = "10:00";
    public static final String NEW_CLOSE_AT = "19:00";
    public static final String NEW_AVAILABLE_TIME_DETAIL = "NEW_AVAIL_TIME_DETAIL";
    public static final Boolean NEW_CCTV_EXISTS = false;
    public static final Boolean NEW_DOOR_LOOK_EXIST = false;
    public static final AirConditioningType NEW_AIR_CONDITIONING_TYPE = AirConditioningType.NONE;
    public static final Boolean NEW_WORKER_EXIST = false;
    public static final Boolean NEW_CAN_PARK = false;
    public static final WarehouseType NEW_WAREHOUSE_TYPE = WarehouseType.FULFILLMENT;
    public static final Integer NEW_MIN_RELEASE_PER_MONTH = 1234;
    public static final Double NEW_LATITUDE = 11.1;
    public static final Double NEW_LONGITUDE = 33.3;
    public static final String NEW_BLOG_URL = "NEW_BLOG_URL";
    public static final WarehouseStatus NEW_WAREHOUSE_STATUS = WarehouseStatus.REJECTED;
    public static final String[] NEW_INSURANCES = {"NEW_INSURANCE_1", "NEW_INSURANCE_2", "NEW_INSURANCE_3"};
    public static final String[] NEW_SECURITY_COMPANIES = {"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"};
    public static final String[] NEW_DELIVERY_TYPES = {"NEW_DELIVERY_1", "NEW_DELIVERY_2", "NEW_DELIVERY_3", "NEW_DELIVERY_4"};
    public static final WarehouseCondition[] NEW_WAREHOUSE_CONDITIONS = {WarehouseCondition.SAVAGE, WarehouseCondition.BONDED, WarehouseCondition.ROOM_TEMPERATURE};
    public static final String[] NEW_WAREHOUSE_FACILITY_USAGES = {"NEW_WH_FACILITY_USAGE_1", "NEW_WH_FACILITY_USAGE_2", "NEW_WH_FACILITY_USAGE_3"};
    public static final String[] NEW_WAREHOUSE_USAGE_CAUTIONS = {"NEW_WH_USAGE_CAUTION_1", "NEW_WH_USAGE_CAUTION_2"};
    public static final MainItemType[] NEW_MAIN_ITEM_TYPES = {MainItemType.FOOD, MainItemType.BOOK, MainItemType.CLOTH};


    private final WarehousesRepository warehousesRepository;

    @Autowired
    public WarehouseEntityFactory(WarehousesRepository warehousesRepository) {
        this.warehousesRepository = warehousesRepository;
    }

    public Warehouses createViewableWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.VIEWABLE, new MainItemType[] {}, true);
    }

    public Warehouses createViewableWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.VIEWABLE, mainItemTypes, true);
    }

    public Warehouses createInProgressWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, new MainItemType[] {}, true);
    }

    public Warehouses createInProgressWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, mainItemTypes, true);
    }

    public Warehouses createdRejectedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.REJECTED, new MainItemType[] {}, true);
    }

    public Warehouses createRejectedWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.REJECTED, mainItemTypes, true);
    }

    public Warehouses createDeletedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.DELETED, new MainItemType[] {}, true);
    }

    public Warehouses createDeletedWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.DELETED, mainItemTypes, true);
    }

    public Warehouses createWarehouseForAdminUpdateTest(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, mainItemTypes, false);
    }

    private Warehouses create(String accessToken, WarehouseStatus status, MainItemType[] mainItemTypes, boolean isUsageAndCautionNull) {
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouses warehouse = Warehouses.builder()
            .userId(userId)
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
            .doorLockExist(DOOR_LOCK_EXIST)
            .airConditioningType(AIR_CONDITIONING_TYPE)
            .workerExist(WORKER_EXIST)
            .canPark(CAN_PARK)
            .warehouseType(WAREHOUSE_TYPE)
            .minReleasePerMonth(MIN_RELEASE_PER_MONTH)
            .latitude(LATITUDE)
            .longitude(LONGITUDE)
            .status(status)
            .build();

        List<MainItemTypes> mainItemTypesList = Arrays.stream(mainItemTypes)
            .map(mainItemType -> new MainItemTypes(mainItemType, warehouse))
            .collect(Collectors.toList());
        warehouse.setMainItemTypes(mainItemTypesList);

        List<WarehouseConditions> warehouseConditions = Arrays.stream(WAREHOUSE_CONDITIONS)
                .map(condition -> new WarehouseConditions(condition, warehouse))
                .collect(Collectors.toList());
        warehouse.setWarehouseConditions(warehouseConditions);

        List<Insurances> insurances = Arrays.stream(INSURANCES)
                .map(insurance -> new Insurances(insurance, warehouse)).collect(Collectors.toList());
        warehouse.setInsurances(insurances);

        List<SecurityCompanies> securityCompanies = Arrays.stream(SECURITY_COMPANIES)
                .map(company -> new SecurityCompanies(company, warehouse)).collect(Collectors.toList());
        warehouse.setSecurityCompanies(securityCompanies);

        List<DeliveryType> deliveryTypes = Arrays.stream(DELIVERY_TYPES)
                .map(type -> new DeliveryType(type, warehouse)).collect(Collectors.toList());
        warehouse.setDeliveryTypes(deliveryTypes);

       if(!isUsageAndCautionNull) {
           List<WarehouseFacilityUsages> warehouseFacilityUsages = Arrays.stream(WAREHOUSE_FACILITY_USAGES)
                   .map(usage -> new WarehouseFacilityUsages(usage, warehouse)).collect(Collectors.toList());
           warehouse.setWarehouseFacilityUsages(warehouseFacilityUsages);

           List<WarehouseUsageCautions> warehouseUsageCautions = Arrays.stream(WAREHOUSE_USAGE_CAUTIONS)
                   .map(caution -> new WarehouseUsageCautions(caution, warehouse)).collect(Collectors.toList());
           warehouse.setWarehouseUsageCautions(warehouseUsageCautions);
       }
        return warehousesRepository.save(warehouse);
    }
}
