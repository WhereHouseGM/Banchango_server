package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.warehouses.dto.WarehouseUpdateRequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UpdateWarehouseTest extends ApiIntegrationTest {

    /**
     * 아래 테스트 메소드는 창고주가 직접 창고를 수정할 수 없기에 주석처리 해놨습니다.
     * 아래 테스트 메소드가 통과하려면 우선 WarehousesService#updateWarehouse의
     * 로직을 AdminService#updateWarehouse처럼 구현해야 합니다.
     */
//    @Test
//    public void put_WarehouseInfoIsUpdated_ifUserIsOwner() {
//        Users owner = userEntityFactory.createUserWithOwnerType();
//        String accessToken = JwtTokenUtil.generateAccessToken(owner);
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
//        Integer warehouseId = warehouse.getId();
//        String url = String.format("/v3/warehouses/%d", warehouseId);
//        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
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
//                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
//                .build();
//
//        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + accessToken)
//                .body(body);
//
//        ResponseEntity<WarehouseDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
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
//        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsNotFound_ifWarehouseNotExist() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Integer warehouseId = 0;
        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsNotFound_ifWarehouseStatusIsDeleted() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();

        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsUnAuthorized_ifTokenNotGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifOtherUsersTokenGiven() {
        User actualOwner = userEntityFactory.createUserWithOwnerType();
        User owner = userEntityFactory.createUserWithOwnerType();

        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        String actualOwnerAccessToken = JwtTokenUtil.generateAccessToken(actualOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(actualOwnerAccessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();

        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer" + accessToken)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifWarehouseStatusIsInProgress() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();

        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifWarehouseStatusIsRejected() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createRejectedWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();

        String url = String.format("/v3/warehouses/%d", warehouseId);
        WarehouseUpdateRequestDto body = WarehouseUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new ItemType[]{ItemType.COSMETIC, ItemType.COLD_STORAGE, ItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
