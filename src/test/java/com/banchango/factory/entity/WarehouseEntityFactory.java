package com.banchango.factory.entity;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehouses.*;
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

    private WarehousesRepository warehousesRepository;

    @Autowired
    public WarehouseEntityFactory(WarehousesRepository warehousesRepository) {
        this.warehousesRepository = warehousesRepository;
    }

    public Warehouses createViewableWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.VIEWABLE, new MainItemType[] {});
    }

    public Warehouses createViewableWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.VIEWABLE, mainItemTypes);
    }

    public Warehouses createInProgressWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, new MainItemType[] {});
    }

    public Warehouses createInProgressWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, mainItemTypes);
    }

    public Warehouses createdRejectedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.REJECTED, new MainItemType[] {});
    }

    public Warehouses createRejectedWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.REJECTED, mainItemTypes);
    }

    public Warehouses createDeletedWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.DELETED, new MainItemType[] {});
    }

    public Warehouses createDeletedWithMainItemTypes(String accessToken, MainItemType[] mainItemTypes) {
        return create(accessToken, WarehouseStatus.DELETED, mainItemTypes);
    }

    private Warehouses create(String accessToken, WarehouseStatus status, MainItemType[] mainItemTypes) {
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
                .map(WarehouseConditions::new)
                .collect(Collectors.toList());
        warehouse.setWarehouseConditions(warehouseConditions);

        List<Insurances> insurances = Arrays.stream(INSURANCES)
                .map(Insurances::new).collect(Collectors.toList());
        warehouse.setInsurances(insurances);

        List<SecurityCompanies> securityCompanies = Arrays.stream(SECURITY_COMPANIES)
                .map(SecurityCompanies::new).collect(Collectors.toList());
        warehouse.setSecurityCompanies(securityCompanies);

        List<DeliveryTypes> deliveryTypes = Arrays.stream(DELIVERY_TYPES)
                .map(DeliveryTypes::new).collect(Collectors.toList());
        warehouse.setDeliveryTypes(deliveryTypes);

        return warehousesRepository.save(warehouse);
    }
}
