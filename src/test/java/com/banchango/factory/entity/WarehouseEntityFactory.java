package com.banchango.factory.entity;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.warehouseconditions.WarehouseConditionType;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsage;
import com.banchango.domain.warehouses.*;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCaution;
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
    public static final WarehouseConditionType[] WAREHOUSE_CONDITIONS = {WarehouseConditionType.ROOM_TEMPERATURE, WarehouseConditionType.LOW_TEMPERATURE};
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
    public static final WarehouseConditionType[] NEW_WAREHOUSE_CONDITIONS = {WarehouseConditionType.SAVAGE, WarehouseConditionType.BONDED, WarehouseConditionType.ROOM_TEMPERATURE};
    public static final String[] NEW_WAREHOUSE_FACILITY_USAGES = {"NEW_WH_FACILITY_USAGE_1", "NEW_WH_FACILITY_USAGE_2", "NEW_WH_FACILITY_USAGE_3"};
    public static final String[] NEW_WAREHOUSE_USAGE_CAUTIONS = {"NEW_WH_USAGE_CAUTION_1", "NEW_WH_USAGE_CAUTION_2"};
    public static final ItemType[] NEW_MAIN_ITEM_TYPES = {ItemType.FOOD, ItemType.BOOK, ItemType.CLOTH};


    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseEntityFactory(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createViewableWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.VIEWABLE, new ItemType[] {}, true);
    }

    public Warehouse createViewableWithMainItemTypes(String accessToken, ItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.VIEWABLE, mainItemTypes, true);
    }

    public Warehouse createInProgressWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, new ItemType[] {}, true);
    }

    public Warehouse createInProgressWithMainItemTypes(String accessToken, ItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, mainItemTypes, true);
    }

    public Warehouse createdRejectedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.REJECTED, new ItemType[] {}, true);
    }

    public Warehouse createRejectedWithMainItemTypes(String accessToken, ItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.REJECTED, mainItemTypes, true);
    }

    public Warehouse createDeletedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.DELETED, new ItemType[] {}, true);
    }

    public Warehouse createDeletedWithMainItemTypes(String accessToken, ItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.DELETED, mainItemTypes, true);
    }

    public Warehouse createWarehouseForAdminUpdateTest(String accessToken, ItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, mainItemTypes, false);
    }

    private Warehouse create(String accessToken, WarehouseStatus status, ItemType[] mainItemTypes, boolean isUsageAndCautionNull) {
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouse warehouse = Warehouse.builder()
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

        List<MainItemType> mainItemTypesList = Arrays.stream(mainItemTypes)
            .map(mainItemType -> new MainItemType(mainItemType, warehouse))
            .collect(Collectors.toList());
        warehouse.setMainItemTypes(mainItemTypesList);

        List<WarehouseCondition> warehouseConditions = Arrays.stream(WAREHOUSE_CONDITIONS)
                .map(condition -> new WarehouseCondition(condition, warehouse))
                .collect(Collectors.toList());
        warehouse.setWarehouseConditions(warehouseConditions);

        List<Insurance> insurances = Arrays.stream(INSURANCES)
                .map(insurance -> new Insurance(insurance, warehouse)).collect(Collectors.toList());
        warehouse.setInsurances(insurances);

        List<SecurityCompany> securityCompanies = Arrays.stream(SECURITY_COMPANIES)
                .map(company -> new SecurityCompany(company, warehouse)).collect(Collectors.toList());
        warehouse.setSecurityCompanies(securityCompanies);

        List<DeliveryType> deliveryTypes = Arrays.stream(DELIVERY_TYPES)
                .map(type -> new DeliveryType(type, warehouse)).collect(Collectors.toList());
        warehouse.setDeliveryTypes(deliveryTypes);

       if(!isUsageAndCautionNull) {
           List<WarehouseFacilityUsage> warehouseFacilityUsages = Arrays.stream(WAREHOUSE_FACILITY_USAGES)
                   .map(usage -> new WarehouseFacilityUsage(usage, warehouse)).collect(Collectors.toList());
           warehouse.setWarehouseFacilityUsages(warehouseFacilityUsages);

           List<WarehouseUsageCaution> warehouseUsageCautions = Arrays.stream(WAREHOUSE_USAGE_CAUTIONS)
                   .map(caution -> new WarehouseUsageCaution(caution, warehouse)).collect(Collectors.toList());
           warehouse.setWarehouseUsageCautions(warehouseUsageCautions);
       }
        return warehouseRepository.save(warehouse);
    }
}
