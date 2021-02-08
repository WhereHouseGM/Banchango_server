package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsage;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AdminUpdateWarehouseTest extends ApiIntegrationTest {

    private WarehouseAdminUpdateRequestDto createUpdateDto() {
        WarehouseAdminUpdateRequestDto body = WarehouseAdminUpdateRequestDto.builder()
                .name(WarehouseEntityFactory.NEW_NAME)
                .space(WarehouseEntityFactory.NEW_SPACE)
                .address(WarehouseEntityFactory.NEW_ADDRESS)
                .addressDetail(WarehouseEntityFactory.NEW_ADDRESS_DETAIL)
                .description(WarehouseEntityFactory.NEW_DESCRIPTION)
                .availableWeekdays(WarehouseEntityFactory.NEW_AVAILABLE_WEEKDAYS)
                .openAt(WarehouseEntityFactory.NEW_OPEN_AT)
                .closeAt(WarehouseEntityFactory.NEW_CLOSE_AT)
                .availableTimeDetail(WarehouseEntityFactory.NEW_AVAILABLE_TIME_DETAIL)
                .cctvExist(WarehouseEntityFactory.NEW_CCTV_EXISTS)
                .doorLockExist(WarehouseEntityFactory.NEW_DOOR_LOOK_EXIST)
                .airConditioningType(WarehouseEntityFactory.NEW_AIR_CONDITIONING_TYPE)
                .workerExist(WarehouseEntityFactory.NEW_WORKER_EXIST)
                .canPark(WarehouseEntityFactory.NEW_CAN_PARK)
                .mainItemTypes(Arrays.asList(WarehouseEntityFactory.NEW_MAIN_ITEM_TYPES))
                .warehouseType(WarehouseEntityFactory.NEW_WAREHOUSE_TYPE)
                .warehouseCondition(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_CONDITIONS))
                .minReleasePerMonth(WarehouseEntityFactory.NEW_MIN_RELEASE_PER_MONTH)
                .latitude(WarehouseEntityFactory.NEW_LATITUDE)
                .longitude(WarehouseEntityFactory.NEW_LONGITUDE)
                .insurances(Arrays.asList(WarehouseEntityFactory.NEW_INSURANCES))
                .securityCompanies(Arrays.asList(WarehouseEntityFactory.NEW_SECURITY_COMPANIES))
                .deliveryTypes(Arrays.asList(WarehouseEntityFactory.NEW_DELIVERY_TYPES))
                .status(WarehouseEntityFactory.NEW_WAREHOUSE_STATUS)
                .blogUrl(WarehouseEntityFactory.NEW_BLOG_URL)
                .warehouseFacilityUsages(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_FACILITY_USAGES))
                .warehouseUsageCautions(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_USAGE_CAUTIONS))
                .build();
        return body;
    }

    private void assertUpdatedWarehouseInfo(Integer warehouseId) {
        Warehouses updatedWarehouse = findWarehouseById.apply(warehouseId);
        assertEquals(WarehouseEntityFactory.NEW_NAME, updatedWarehouse.getName());
        assertEquals(WarehouseEntityFactory.NEW_SPACE, updatedWarehouse.getSpace());
        assertEquals(WarehouseEntityFactory.NEW_ADDRESS, updatedWarehouse.getAddress());
        assertEquals(WarehouseEntityFactory.NEW_ADDRESS_DETAIL, updatedWarehouse.getAddressDetail());
        assertEquals(WarehouseEntityFactory.NEW_DESCRIPTION, updatedWarehouse.getDescription());
        assertEquals(WarehouseEntityFactory.NEW_AVAILABLE_WEEKDAYS, updatedWarehouse.getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.NEW_OPEN_AT, updatedWarehouse.getOpenAt());
        assertEquals(WarehouseEntityFactory.NEW_CLOSE_AT, updatedWarehouse.getCloseAt());
        assertEquals(WarehouseEntityFactory.NEW_AVAILABLE_TIME_DETAIL, updatedWarehouse.getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.NEW_CCTV_EXISTS, updatedWarehouse.getCctvExist());
        assertEquals(WarehouseEntityFactory.NEW_DOOR_LOOK_EXIST, updatedWarehouse.getDoorLockExist());
        assertEquals(WarehouseEntityFactory.NEW_AIR_CONDITIONING_TYPE, updatedWarehouse.getAirConditioningType());
        assertEquals(WarehouseEntityFactory.NEW_WORKER_EXIST, updatedWarehouse.getWorkerExist());
        assertEquals(WarehouseEntityFactory.NEW_CAN_PARK, updatedWarehouse.getCanPark());
        assertEquals(WarehouseEntityFactory.NEW_WAREHOUSE_TYPE, updatedWarehouse.getWarehouseType());
        assertEquals(WarehouseEntityFactory.NEW_WAREHOUSE_STATUS, updatedWarehouse.getStatus());
        assertEquals(WarehouseEntityFactory.NEW_LONGITUDE, updatedWarehouse.getLongitude());
        assertEquals(WarehouseEntityFactory.NEW_LATITUDE, updatedWarehouse.getLatitude());
        assertEquals(WarehouseEntityFactory.NEW_BLOG_URL, updatedWarehouse.getBlogUrl());
        assertEquals(WarehouseEntityFactory.NEW_MIN_RELEASE_PER_MONTH, updatedWarehouse.getMinReleasePerMonth());
        assertEquals(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurance::getName).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_INSURANCES));
        assertEquals(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompany::getName).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_SECURITY_COMPANIES));
        assertEquals(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryType::getName).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_DELIVERY_TYPES));
        assertEquals(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseFacilityUsage::getContent).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_FACILITY_USAGES));
        assertEquals(warehouseUsageCautionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseUsageCautions::getContent).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_USAGE_CAUTIONS));
        assertEquals(warehouseConditionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseCondition::getCondition).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_CONDITIONS));
        assertEquals(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemType::getType).collect(Collectors.toList()), Arrays.asList(WarehouseEntityFactory.NEW_MAIN_ITEM_TYPES));
    }

    private void assertResponse(WarehouseAdminDetailResponseDto dto) {
        assertEquals(WarehouseEntityFactory.NEW_NAME, dto.getName());
        assertEquals(WarehouseEntityFactory.NEW_SPACE, dto.getSpace());
        assertEquals(WarehouseEntityFactory.NEW_ADDRESS, dto.getAddress());
        assertEquals(WarehouseEntityFactory.NEW_ADDRESS_DETAIL, dto.getAddressDetail());
        assertEquals(WarehouseEntityFactory.NEW_DESCRIPTION, dto.getDescription());
        assertEquals(WarehouseEntityFactory.NEW_AVAILABLE_WEEKDAYS, dto.getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.NEW_OPEN_AT, dto.getOpenAt());
        assertEquals(WarehouseEntityFactory.NEW_CLOSE_AT, dto.getCloseAt());
        assertEquals(WarehouseEntityFactory.NEW_AVAILABLE_TIME_DETAIL, dto.getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.NEW_CCTV_EXISTS, dto.getCctvExist());
        assertEquals(WarehouseEntityFactory.NEW_DOOR_LOOK_EXIST, dto.getDoorLockExist());
        assertEquals(WarehouseEntityFactory.NEW_WORKER_EXIST, dto.getWorkerExist());
        assertEquals(WarehouseEntityFactory.NEW_CAN_PARK, dto.getCanPark());
        assertEquals(WarehouseEntityFactory.NEW_AIR_CONDITIONING_TYPE, dto.getAirConditioningType());
        assertEquals(WarehouseEntityFactory.NEW_WAREHOUSE_TYPE, dto.getWarehouseType());
        assertEquals(WarehouseEntityFactory.NEW_MIN_RELEASE_PER_MONTH, dto.getMinReleasePerMonth());
        assertEquals(WarehouseEntityFactory.NEW_LATITUDE, dto.getLatitude());
        assertEquals(WarehouseEntityFactory.NEW_LONGITUDE, dto.getLongitude());
        assertEquals(WarehouseEntityFactory.NEW_BLOG_URL, dto.getBlogUrl());
        assertEquals(WarehouseEntityFactory.NEW_WAREHOUSE_STATUS, dto.getStatus());
        assertEquals(dto.getInsurances(), Arrays.asList(WarehouseEntityFactory.NEW_INSURANCES));
        assertEquals(dto.getSecurityCompanies(), Arrays.asList(WarehouseEntityFactory.NEW_SECURITY_COMPANIES));
        assertEquals(dto.getDeliveryTypes(), Arrays.asList(WarehouseEntityFactory.NEW_DELIVERY_TYPES));
        assertEquals(dto.getWarehouseFacilityUsages(), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_FACILITY_USAGES));
        assertEquals(dto.getWarehouseUsageCautions(), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_USAGE_CAUTIONS));
        assertEquals(dto.getWarehouseCondition(), Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_CONDITIONS));
        assertEquals(dto.getMainItemTypes(), Arrays.asList(WarehouseEntityFactory.NEW_MAIN_ITEM_TYPES));
    }

    @Test
    public void put_WarehouseInfoUpdate_responseIdOk_ifAllConditionsAreRightAndAdminIsOwner() {
        User user = userEntityFactory.createUserWithOwnerType();
        String userAccessToken = JwtTokenUtil.generateAccessToken(user);
        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
        Warehouses warehouse = warehouseEntityFactory.createWarehouseForAdminUpdateTest(userAccessToken, new ItemType[]{ItemType.FOOD, ItemType.ELECTRONICS});
        Integer warehouseId = warehouse.getId();

        List<Integer> beforeInsurancesId = insurancesRepository.findByWarehouseId(warehouseId)
                .stream().map(Insurance::getId).collect(Collectors.toList());

        List<Integer> beforeSecurityCompaniesId = securityCompaniesRepository.findByWarehouseId(warehouseId)
                .stream().map(SecurityCompany::getId).collect(Collectors.toList());

        List<Integer> beforeDeliveryTypesId = deliveryTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(DeliveryType::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseConditionsId = warehouseConditionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseCondition::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseFacilityUsagesId = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseFacilityUsage::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseUsageCautionsId = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseUsageCautions::getId).collect(Collectors.toList());

        List<Integer> beforeMainItemTypesId = mainItemTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(MainItemType::getId).collect(Collectors.toList());

        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        WarehouseAdminUpdateRequestDto body = createUpdateDto();
        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(body);

        ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertResponse(response.getBody());

        assertUpdatedWarehouseInfo(warehouseId);

        List<Integer> updatedInsuranceId = insurancesRepository.findByWarehouseId(warehouseId)
                .stream().map(Insurance::getId).collect(Collectors.toList());

        List<Integer> updatedSecurityCompaniesId = securityCompaniesRepository.findByWarehouseId(warehouseId)
                .stream().map(SecurityCompany::getId).collect(Collectors.toList());

        List<Integer> updatedDeliveryTypesId = deliveryTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(DeliveryType::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseConditionsId = warehouseConditionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseCondition::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseFacilityUsagesId = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseFacilityUsage::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseUsageCautionsId = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseUsageCautions::getId).collect(Collectors.toList());

        List<Integer> updatedMainItemTypesId = mainItemTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(MainItemType::getId).collect(Collectors.toList());

        // Index, size Assertions

        // Insurances : 인덱스 동일한가 체크
        assertEquals(beforeInsurancesId.size(), updatedInsuranceId.size());
        assertEquals(beforeInsurancesId, updatedInsuranceId);

        // SecurityCompanies : 개수 1개 적음
        assertEquals(beforeSecurityCompaniesId.size() - 1, updatedSecurityCompaniesId.size());
        assertTrue(beforeSecurityCompaniesId.containsAll(updatedSecurityCompaniesId));

        // DeliveryTypes : 개수 1개 많음
        assertEquals(beforeDeliveryTypesId.size() + 1, updatedDeliveryTypesId.size());
        assertTrue(updatedDeliveryTypesId.containsAll(beforeDeliveryTypesId));

        // WarehouseConditions: 개수 1개 많음
        assertEquals(beforeWarehouseConditionsId.size() + 1, updatedWarehouseConditionsId.size());
        assertTrue(updatedWarehouseConditionsId.containsAll(beforeWarehouseConditionsId));

        // WarehouseFacilityUsages: 개수 2개 많음
        assertEquals(beforeWarehouseFacilityUsagesId.size() + 2, updatedWarehouseFacilityUsagesId.size());
        assertTrue(updatedWarehouseFacilityUsagesId.containsAll(beforeWarehouseFacilityUsagesId));

        // WarehouseUsageCautions: 개수 1개 적음
        assertEquals(beforeWarehouseUsageCautionsId.size() - 1, updatedWarehouseUsageCautionsId.size());
        assertTrue(beforeWarehouseUsageCautionsId.containsAll(updatedWarehouseUsageCautionsId));

        // MainItemTypes: 개수 1개 많음
        assertEquals(beforeMainItemTypesId.size() + 1, updatedMainItemTypesId.size());
        assertTrue(updatedMainItemTypesId.containsAll(beforeMainItemTypesId));

    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsOk_ifAllConditionsAreRightAndAdminIsShipper() {
        User user = userEntityFactory.createUserWithOwnerType();
        String userAccessToken = JwtTokenUtil.generateAccessToken(user);
        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
        Warehouses warehouse = warehouseEntityFactory.createWarehouseForAdminUpdateTest(userAccessToken, new ItemType[]{ItemType.FOOD, ItemType.BOOK});
        Integer warehouseId = warehouse.getId();

        List<Integer> beforeInsurancesId = insurancesRepository.findByWarehouseId(warehouseId)
                .stream().map(Insurance::getId).collect(Collectors.toList());

        List<Integer> beforeSecurityCompaniesId = securityCompaniesRepository.findByWarehouseId(warehouseId)
                .stream().map(SecurityCompany::getId).collect(Collectors.toList());

        List<Integer> beforeDeliveryTypesId = deliveryTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(DeliveryType::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseConditionsId = warehouseConditionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseCondition::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseFacilityUsagesId = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseFacilityUsage::getId).collect(Collectors.toList());

        List<Integer> beforeWarehouseUsageCautionsId = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseUsageCautions::getId).collect(Collectors.toList());

        List<Integer> beforeMainItemTypesId = mainItemTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(MainItemType::getId).collect(Collectors.toList());

        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        WarehouseAdminUpdateRequestDto body = createUpdateDto();
        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(body);

        ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertResponse(response.getBody());

        assertUpdatedWarehouseInfo(warehouseId);

        List<Integer> updatedInsuranceId = insurancesRepository.findByWarehouseId(warehouseId)
                .stream().map(Insurance::getId).collect(Collectors.toList());

        List<Integer> updatedSecurityCompaniesId = securityCompaniesRepository.findByWarehouseId(warehouseId)
                .stream().map(SecurityCompany::getId).collect(Collectors.toList());

        List<Integer> updatedDeliveryTypesId = deliveryTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(DeliveryType::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseConditionsId = warehouseConditionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseCondition::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseFacilityUsagesId = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseFacilityUsage::getId).collect(Collectors.toList());

        List<Integer> updatedWarehouseUsageCautionsId = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId)
                .stream().map(WarehouseUsageCautions::getId).collect(Collectors.toList());

        List<Integer> updatedMainItemTypesId = mainItemTypesRepository.findByWarehouseId(warehouseId)
                .stream().map(MainItemType::getId).collect(Collectors.toList());

        // Index, size Assertions

        // Insurances : 인덱스 동일한가 체크
        assertEquals(beforeInsurancesId.size(), updatedInsuranceId.size());
        assertEquals(beforeInsurancesId, updatedInsuranceId);

        // SecurityCompanies : 개수 1개 적음
        assertEquals(beforeSecurityCompaniesId.size() - 1, updatedSecurityCompaniesId.size());
        assertTrue(beforeSecurityCompaniesId.containsAll(updatedSecurityCompaniesId));

        // DeliveryTypes : 개수 1개 많음
        assertEquals(beforeDeliveryTypesId.size() + 1, updatedDeliveryTypesId.size());
        assertTrue(updatedDeliveryTypesId.containsAll(beforeDeliveryTypesId));

        // WarehouseConditions: 개수 1개 많음
        assertEquals(beforeWarehouseConditionsId.size() + 1, updatedWarehouseConditionsId.size());
        assertTrue(updatedWarehouseConditionsId.containsAll(beforeWarehouseConditionsId));

        // WarehouseFacilityUsages: 개수 2개 많음
        assertEquals(beforeWarehouseFacilityUsagesId.size() + 2, updatedWarehouseFacilityUsagesId.size());
        assertTrue(updatedWarehouseFacilityUsagesId.containsAll(beforeWarehouseFacilityUsagesId));

        // WarehouseUsageCautions: 개수 1개 적음
        assertEquals(beforeWarehouseUsageCautionsId.size() - 1, updatedWarehouseUsageCautionsId.size());
        assertTrue(beforeWarehouseUsageCautionsId.containsAll(updatedWarehouseUsageCautionsId));

        // MainItemTypes: 개수 1개 많음
        assertEquals(beforeMainItemTypesId.size() + 1, updatedMainItemTypesId.size());
        assertTrue(updatedMainItemTypesId.containsAll(beforeMainItemTypesId));
    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsUnAuthorized_ifTokenNotGiven() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);

        WarehouseAdminUpdateRequestDto body = createUpdateDto();

        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsForbidden_ifNotAdmin() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);

        WarehouseAdminUpdateRequestDto body = createUpdateDto();

        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsNotFound_ifWarehouseNotExist() {

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Integer warehouseId = 0;
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);

        WarehouseAdminUpdateRequestDto body = createUpdateDto();

        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .header("Authorization", "Bearer "+ adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
