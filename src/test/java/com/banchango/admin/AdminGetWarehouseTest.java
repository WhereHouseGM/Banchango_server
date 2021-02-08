package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AdminGetWarehouseTest extends ApiIntegrationTest {

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsOk_ifAdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d",warehouseId);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(request, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WarehouseEntityFactory.NAME, response.getBody().getName());
        assertEquals(WarehouseEntityFactory.SPACE, response.getBody().getSpace());
        assertEquals(WarehouseEntityFactory.ADDRESS, response.getBody().getAddress());
        assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, response.getBody().getAddressDetail());
        assertEquals(WarehouseEntityFactory.DESCRIPTION, response.getBody().getDescription());
        assertEquals(WarehouseEntityFactory.AVAILABLE_WEEKDAYS, response.getBody().getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.OPEN_AT, response.getBody().getOpenAt());
        assertEquals(WarehouseEntityFactory.CLOSE_AT, response.getBody().getCloseAt());
        assertEquals(WarehouseEntityFactory.AVAILABLE_TIME_DETAIL, response.getBody().getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.CCTV_EXISTS, response.getBody().getCctvExist());
        assertEquals(WarehouseEntityFactory.DOOR_LOCK_EXIST, response.getBody().getDoorLockExist());
        assertEquals(WarehouseEntityFactory.AIR_CONDITIONING_TYPE, response.getBody().getAirConditioningType());
        assertEquals(WarehouseEntityFactory.WORKER_EXIST, response.getBody().getWorkerExist());
        assertEquals(WarehouseEntityFactory.CAN_PARK, response.getBody().getCanPark());
        assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, response.getBody().getWarehouseType());
        assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, response.getBody().getMinReleasePerMonth());
        assertEquals(WarehouseEntityFactory.LATITUDE, response.getBody().getLatitude());
        assertEquals(WarehouseEntityFactory.LONGITUDE, response.getBody().getLongitude());
        assertEquals(response.getBody().getDeliveryTypes(), Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES));
        assertEquals(response.getBody().getInsurances(), Arrays.asList(WarehouseEntityFactory.INSURANCES));
        assertEquals(response.getBody().getSecurityCompanies(), Arrays.asList(WarehouseEntityFactory.SECURITY_COMPANIES));
        assertNotNull(response.getBody().getCreatedAt());
        assertEquals(response.getBody().getWarehouseCondition(), Arrays.asList(WarehouseEntityFactory.WAREHOUSE_CONDITIONS));
        assertEquals(WarehouseStatus.VIEWABLE, response.getBody().getStatus());
        assertNull(response.getBody().getBlogUrl());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsOk_ifAdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d",warehouseId);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(request, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WarehouseEntityFactory.NAME, response.getBody().getName());
        assertEquals(WarehouseEntityFactory.SPACE, response.getBody().getSpace());
        assertEquals(WarehouseEntityFactory.ADDRESS, response.getBody().getAddress());
        assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, response.getBody().getAddressDetail());
        assertEquals(WarehouseEntityFactory.DESCRIPTION, response.getBody().getDescription());
        assertEquals(WarehouseEntityFactory.AVAILABLE_WEEKDAYS, response.getBody().getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.OPEN_AT, response.getBody().getOpenAt());
        assertEquals(WarehouseEntityFactory.CLOSE_AT, response.getBody().getCloseAt());
        assertEquals(WarehouseEntityFactory.AVAILABLE_TIME_DETAIL, response.getBody().getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.CCTV_EXISTS, response.getBody().getCctvExist());
        assertEquals(WarehouseEntityFactory.DOOR_LOCK_EXIST, response.getBody().getDoorLockExist());
        assertEquals(WarehouseEntityFactory.AIR_CONDITIONING_TYPE, response.getBody().getAirConditioningType());
        assertEquals(WarehouseEntityFactory.WORKER_EXIST, response.getBody().getWorkerExist());
        assertEquals(WarehouseEntityFactory.CAN_PARK, response.getBody().getCanPark());
        assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, response.getBody().getWarehouseType());
        assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, response.getBody().getMinReleasePerMonth());
        assertEquals(WarehouseEntityFactory.LATITUDE, response.getBody().getLatitude());
        assertEquals(WarehouseEntityFactory.LONGITUDE, response.getBody().getLongitude());
        assertEquals(response.getBody().getDeliveryTypes(), Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES));
        assertEquals(response.getBody().getInsurances(), Arrays.asList(WarehouseEntityFactory.INSURANCES));
        assertEquals(response.getBody().getSecurityCompanies(), Arrays.asList(WarehouseEntityFactory.SECURITY_COMPANIES));
        assertNotNull(response.getBody().getCreatedAt());
        assertEquals(response.getBody().getWarehouseCondition(), Arrays.asList(WarehouseEntityFactory.WAREHOUSE_CONDITIONS));
        assertEquals(WarehouseStatus.VIEWABLE, response.getBody().getStatus());
        assertNull(response.getBody().getBlogUrl());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsNotFound_IfWarehouseNotExist() {

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        String url = String.format("/v3/admin/warehouses/%d", 0);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsForbidden_IfTokenIsWrong() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[]{ItemType.BOOK, ItemType.FOOD, ItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
