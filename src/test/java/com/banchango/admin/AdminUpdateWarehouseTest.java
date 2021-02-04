package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautionsRepository;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Arrays;
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
        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
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
        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.NEW_INSURANCES)));
        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.NEW_SECURITY_COMPANIES)));
        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.NEW_DELIVERY_TYPES)));
        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseFacilityUsages::getContent).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_FACILITY_USAGES)));
        assertTrue(warehouseUsageCautionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseUsageCautions::getContent).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_USAGE_CAUTIONS)));
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
        assertTrue(dto.getInsurances().containsAll(Arrays.asList(WarehouseEntityFactory.NEW_INSURANCES)));
        assertTrue(dto.getSecurityCompanies().containsAll(Arrays.asList(WarehouseEntityFactory.NEW_SECURITY_COMPANIES)));
        assertTrue(dto.getDeliveryTypes().containsAll(Arrays.asList(WarehouseEntityFactory.NEW_DELIVERY_TYPES)));
        assertTrue(dto.getWarehouseFacilityUsages().containsAll(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_FACILITY_USAGES)));
        assertTrue(dto.getWarehouseUsageCautions().containsAll(Arrays.asList(WarehouseEntityFactory.NEW_WAREHOUSE_USAGE_CAUTIONS)));
    }

//    private WarehouseAdminUpdateRequestDto createWarehouseAdminRequestDto(Boolean isBlogUrlNull) {
//        WarehouseAdminUpdateRequestDto body = WarehouseAdminUpdateRequestDto.builder()
//                .name("NEW NAME")
//                .space(999)
//                .address("NEW ADDRESS")
//                .addressDetail("NEW ADDR_DETAIL")
//                .description("NEW DESC")
//                .availableWeekdays(101010)
//                .openAt("08:00")
//                .closeAt("23:30")
//                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
//                .cctvExist(false)
//                .doorLockExist(false)
//                .airConditioningType(AirConditioningType.NONE)
//                .workerExist(false)
//                .canPark(false)
//                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
//                .warehouseType(WarehouseType.FULFILLMENT)
//                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
//                .minReleasePerMonth(101)
//                .latitude(11.11)
//                .longitude(33.33)
//                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
//                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
//                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
//                .status(WarehouseStatus.REJECTED)
//                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
//                .build();
//        if (!isBlogUrlNull) {
//            body.setBlogUrl("BLOG_URL");
//        }
//        return body;
//    }
//
//    @Test
//    public void put_WarehouseInfoIsUpdatedWithoutBlogUrl_responseIsOk_ifAdminIsOwner() {
//        Users user = userEntityFactory.createUserWithOwnerType();
//        String accessToken = JwtTokenUtil.generateAccessToken(user);
//
//        Users admin = userEntityFactory.createAdminWithOwnerType();
//        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
//
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
//        Integer warehouseId = warehouse.getId();
//        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
//        WarehouseAdminUpdateRequestDto body = createWarehouseAdminRequestDto(true);
//
//        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + adminAccessToken)
//                .body(body);
//
//        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
//        assertEquals("NEW NAME", firstResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", firstResponse.getBody().getOpenAt());
//        assertEquals("23:30", firstResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
//        assertFalse(firstResponse.getBody().getCctvExist());
//        assertFalse(firstResponse.getBody().getDoorLockExist());
//        assertFalse(firstResponse.getBody().getWorkerExist());
//        assertFalse(firstResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
//        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
//        assertNull(firstResponse.getBody().getBlogUrl());
//        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
//        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//
//        assertUpdatedWarehouseInfo(warehouseId, true);
//    }
//
//    @Test
//    public void put_WarehouseInfoIsUpdatedWithoutBlogUrl_responseIsOk_ifAdminIsShipper() {
//        Users user = userEntityFactory.createUserWithOwnerType();
//        String accessToken = JwtTokenUtil.generateAccessToken(user);
//
//        Users admin = userEntityFactory.createAdminWithShipperType();
//        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
//
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
//        Integer warehouseId = warehouse.getId();
//        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
//        WarehouseAdminUpdateRequestDto body = createWarehouseAdminRequestDto(true);
//        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + adminAccessToken)
//                .body(body);
//
//        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
//        assertEquals("NEW NAME", firstResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", firstResponse.getBody().getOpenAt());
//        assertEquals("23:30", firstResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
//        assertFalse(firstResponse.getBody().getCctvExist());
//        assertFalse(firstResponse.getBody().getDoorLockExist());
//        assertFalse(firstResponse.getBody().getWorkerExist());
//        assertFalse(firstResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
//        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
//        assertNull(firstResponse.getBody().getBlogUrl());
//        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
//        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//
//        assertUpdatedWarehouseInfo(warehouseId, true);
//    }
//
//    @Test
//    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsOk_ifAdminIsOwner() {
//        Users user = userEntityFactory.createUserWithOwnerType();
//        String accessToken = JwtTokenUtil.generateAccessToken(user);
//
//        Users admin = userEntityFactory.createAdminWithOwnerType();
//        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
//
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
//        Integer warehouseId = warehouse.getId();
//        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
//
//        WarehouseAdminUpdateRequestDto body = createWarehouseAdminRequestDto(false);
//
//        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer " + adminAccessToken)
//            .body(body);
//
//        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
//        assertEquals("NEW NAME", firstResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", firstResponse.getBody().getOpenAt());
//        assertEquals("23:30", firstResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
//        assertFalse(firstResponse.getBody().getCctvExist());
//        assertFalse(firstResponse.getBody().getDoorLockExist());
//        assertFalse(firstResponse.getBody().getWorkerExist());
//        assertFalse(firstResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
//        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
//        assertEquals("BLOG_URL", firstResponse.getBody().getBlogUrl());
//        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
//        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//
//        assertUpdatedWarehouseInfo(warehouseId, false);
//    }
//
//    @Test
//    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsOk_ifAdminIsShipper() {
//        Users user = userEntityFactory.createUserWithOwnerType();
//        String accessToken = JwtTokenUtil.generateAccessToken(user);
//
//        Users admin = userEntityFactory.createAdminWithShipperType();
//        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
//
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
//        Integer warehouseId = warehouse.getId();
//        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
//
//        WarehouseAdminUpdateRequestDto body = createWarehouseAdminRequestDto(false);
//
//        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + adminAccessToken)
//                .body(body);
//
//        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
//        assertEquals("NEW NAME", firstResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", firstResponse.getBody().getOpenAt());
//        assertEquals("23:30", firstResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
//        assertFalse(firstResponse.getBody().getCctvExist());
//        assertFalse(firstResponse.getBody().getDoorLockExist());
//        assertFalse(firstResponse.getBody().getWorkerExist());
//        assertFalse(firstResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
//        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
//        assertEquals("BLOG_URL", firstResponse.getBody().getBlogUrl());
//        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
//        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//
//        assertUpdatedWarehouseInfo(warehouseId, false);
//    }
//

    @Test
    public void put_WarehouseInfoUpdate_responseIdOk_ifAllConditionsAreRightAndAdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String userAccessToken = JwtTokenUtil.generateAccessToken(user);
        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
        Warehouses warehouse = warehouseEntityFactory.createWarehouseForAdminUpdateTest(userAccessToken, new MainItemType[]{MainItemType.FOOD, MainItemType.ELECTRONICS});
        Integer warehouseId = warehouse.getId();

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
        // Insurances : 개수 동일, 값만 업데이트 체크 => 인덱스 동일해야함
        // SecurityCompanies : 개수 1개 적음, 값 업데이트 체크, 인덱스 기존 거만 있어야함
        // DeliveryTypes: 개수 1개 많음 => 인덱스 동일 및 1개 추가
        // WarehouseConditions: 개수 1개 많음
        // WarehouseFacilityUsages: 개수 2개 많음
        // WarehouseUsageCautions: 개수 1개 적음
        // MainItemTypes: 개수 1개 많음

    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsOk_ifAllConditionsAreRightAndAdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String userAccessToken = JwtTokenUtil.generateAccessToken(user);
        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);
        Warehouses warehouse = warehouseEntityFactory.createWarehouseForAdminUpdateTest(userAccessToken, new MainItemType[]{MainItemType.FOOD, MainItemType.BOOK, MainItemType.ELECTRONICS});
        Integer warehouseId = warehouse.getId();

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

    }

    @Test
    public void put_WarehouseInfoUpdate_responseIsUnAuthorized_ifTokenNotGiven() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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

        Users admin = userEntityFactory.createAdminWithOwnerType();
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
