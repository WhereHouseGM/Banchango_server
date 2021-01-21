package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GetWarehouseDetailTest extends ApiIntegrationTest {

    @Test
    public void get_warehouseDetail_responseIsOk_IfUserIsOwner() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses _warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        WarehouseDetailResponseDto warehouse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(warehouse.getWarehouseId());
        assertNotNull(warehouse.getOwnerId());
        assertNotNull(warehouse.getName());
        assertNotNull(warehouse.getSpace());
        assertNotNull(warehouse.getAddress());
        assertNotNull(warehouse.getAddressDetail());
        assertNotNull(warehouse.getDescription());
        assertNotNull(warehouse.getAvailableWeekdays());
        assertNotNull(warehouse.getOpenAt());
        assertNotNull(warehouse.getCloseAt());
        assertNotNull(warehouse.getAvailableTimeDetail());
        assertNotNull(warehouse.getCctvExist());
        assertNotNull(warehouse.getDoorLockExist());
        assertNotNull(warehouse.getAirConditioningType());
        assertNotNull(warehouse.getWorkerExist());
        assertNotNull(warehouse.getCanPark());
        assertNotNull(warehouse.getMainItemTypes());
        assertNotNull(warehouse.getWarehouseType());
        assertNotNull(warehouse.getMinReleasePerMonth());
        assertNotNull(warehouse.getLatitude());
        assertNotNull(warehouse.getLongitude());
        assertNull(warehouse.getBlogUrl());
        assertNotNull(warehouse.getMainImageUrl());
        assertNotNull(warehouse.getDeliveryTypes());
        assertNotNull(warehouse.getWarehouseCondition());
        assertNotNull(warehouse.getWarehouseFacilityUsages());
        assertNotNull(warehouse.getWarehouseUsageCautions());
        assertNotNull(warehouse.getImages());

        warehousesRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsOk_IfUserIsShipper() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        WarehouseDetailResponseDto warehouse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(warehouse.getWarehouseId());
        assertNotNull(warehouse.getOwnerId());
        assertNotNull(warehouse.getName());
        assertNotNull(warehouse.getSpace());
        assertNotNull(warehouse.getAddress());
        assertNotNull(warehouse.getAddressDetail());
        assertNotNull(warehouse.getDescription());
        assertNotNull(warehouse.getAvailableWeekdays());
        assertNotNull(warehouse.getOpenAt());
        assertNotNull(warehouse.getCloseAt());
        assertNotNull(warehouse.getAvailableTimeDetail());
        assertNotNull(warehouse.getCctvExist());
        assertNotNull(warehouse.getDoorLockExist());
        assertNotNull(warehouse.getAirConditioningType());
        assertNotNull(warehouse.getWorkerExist());
        assertNotNull(warehouse.getCanPark());
        assertNotNull(warehouse.getMainItemTypes());
        assertNotNull(warehouse.getWarehouseType());
        assertNotNull(warehouse.getMinReleasePerMonth());
        assertNotNull(warehouse.getLatitude());
        assertNotNull(warehouse.getLongitude());
        assertNull(warehouse.getBlogUrl());
        assertNotNull(warehouse.getMainImageUrl());
        assertNotNull(warehouse.getDeliveryTypes());
        assertNotNull(warehouse.getWarehouseCondition());
        assertNotNull(warehouse.getWarehouseFacilityUsages());
        assertNotNull(warehouse.getWarehouseUsageCautions());
        assertNotNull(warehouse.getImages());

        warehousesRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        warehousesRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfIsViewableIsRejected() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        warehousesRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        warehousesRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseNotExist() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        String url = String.format("/v3/warehouses/%d", 0);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
