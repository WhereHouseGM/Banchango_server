package com.banchango.factory.entity;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.warehouses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehouseEntityFactory {
    private static final String NAME = "NAME";
    private static final Integer SPACE = 123;
    private static final String ADDRESS = "ADDRESS";
    private static final String ADDRESS_DETAIL = "ADDRESS_DETAIL";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final Integer AVAILABLE_WEEKDAYS = 1;
    private static final String OPEN_AT = "09:00";
    private static final String CLOSE_AT = "18:00";
    private static final String AVAILABLE_TIME_DETAIL = "AVAILABLE_TIME_DETAIL";
    private static final String INSURANCE = "INSURANCE";
    private static final Boolean CCTV_EXISTS = true;
    private static final String SECURITY_COMPANY_NAME = "SECURITY_COMPANY_NAME";
    private static final Boolean DDOR_LOCK_EXIST = true;
    private static final AirConditioningType AIR_CONDITIONING_TYPE = AirConditioningType.BOTH;
    private static final Boolean WORKER_EXIST = true;
    private static final Boolean CAN_PARK = true;
    private static final WarehouseType WAREHOUSE_TYPE = WarehouseType.THREEPL;
    private static final Integer MIN_RELEASE_PER_MONTH = 1000;
    private static final Double LATITUDE = 22.2;
    private static final Double LONGITUDE = 22.2;

    private WarehousesRepository warehousesRepository;

    @Autowired
    public WarehouseEntityFactory(WarehousesRepository warehousesRepository) {
        this.warehousesRepository = warehousesRepository;
    }

    public Warehouses createViewableWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.VIEWABLE, new MainItemType[] {});
    }

    public Warehouses createInProgressWithNoMainItemTypes(String accessToken) {
        return create(accessToken, WarehouseStatus.IN_PROGRESS, new MainItemType[] {});
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
            .insurance(INSURANCE)
            .cctvExist(CCTV_EXISTS)
            .securityCompanyName(SECURITY_COMPANY_NAME)
            .doorLockExist(DDOR_LOCK_EXIST)
            .airConditioningType(AIR_CONDITIONING_TYPE)
            .workerExist(WORKER_EXIST)
            .canPark(CAN_PARK)
            .warehouseType(WAREHOUSE_TYPE)
            .minReleasePerMonth(MIN_RELEASE_PER_MONTH)
            .latitude(LATITUDE)
            .longitude(LONGITUDE)
            .status(status)
            .build();

        List<MainItemTypes> m = Arrays.stream(mainItemTypes)
            .map(mainItemType -> new MainItemTypes(mainItemType, warehouse))
            .collect(Collectors.toList());

        warehouse.getMainItemTypes().addAll(m);

        return warehousesRepository.save(warehouse);
    }
}
