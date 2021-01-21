package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseUpdateRequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UpdateWarehouseTest extends ApiIntegrationTest {

    @Test
    public void put_WarehouseInfoIsUpdated_ifUserIsOwner() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
        assertEquals("NEW NAME", firstResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", firstResponse.getBody().getOpenAt());
        assertEquals("23:30", firstResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
        assertFalse(firstResponse.getBody().getCctvExist());
        assertFalse(firstResponse.getBody().getDoorLockExist());
        assertFalse(firstResponse.getBody().getWorkerExist());
        assertFalse(firstResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
        assertNull(firstResponse.getBody().getBlogUrl());
        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
                header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<WarehouseDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals("NEW NAME", secondResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", secondResponse.getBody().getOpenAt());
        assertEquals("23:30", secondResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
        assertFalse(secondResponse.getBody().getCctvExist());
        assertFalse(secondResponse.getBody().getDoorLockExist());
        assertFalse(secondResponse.getBody().getWorkerExist());
        assertFalse(secondResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
        assertNull(secondResponse.getBody().getBlogUrl());
        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsNotFound_ifWarehouseNotExist() {
        Users owner = userEntityFactory.createUserWithOwnerType();
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsNotFound_ifWarehouseStatusIsDeleted() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsUnAuthorized_ifTokenNotGiven() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifOtherUsersTokenGiven() {
        Users actualOwner = userEntityFactory.createUserWithOwnerType();
        Users owner = userEntityFactory.createUserWithOwnerType();

        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        String actualOwnerAccessToken = JwtTokenUtil.generateAccessToken(actualOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(actualOwnerAccessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifWarehouseStatusIsInProgress() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdated_responseIsForbidden_ifWarehouseStatusIsRejected() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createRejectedWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
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
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
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

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(putRequest, WarehouseDetailResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
