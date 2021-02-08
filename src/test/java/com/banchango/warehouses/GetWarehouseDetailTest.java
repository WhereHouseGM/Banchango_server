package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GetWarehouseDetailTest extends ApiIntegrationTest {

    private void assertWarehouseDetail(Warehouses warehouse) {
        Integer warehouseId = warehouse.getId();
        assertEquals(WarehouseEntityFactory.NAME, warehouse.getName());
        assertEquals(WarehouseEntityFactory.SPACE, warehouse.getSpace());
        assertEquals(WarehouseEntityFactory.ADDRESS, warehouse.getAddress());
        assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, warehouse.getAddressDetail());
        assertEquals(WarehouseEntityFactory.DESCRIPTION, warehouse.getDescription());
        assertEquals(WarehouseEntityFactory.AVAILABLE_WEEKDAYS, warehouse.getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.OPEN_AT, warehouse.getOpenAt());
        assertEquals(WarehouseEntityFactory.CLOSE_AT, warehouse.getCloseAt());
        assertEquals(WarehouseEntityFactory.AVAILABLE_TIME_DETAIL, warehouse.getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.CCTV_EXISTS, warehouse.getCctvExist());
        assertEquals(WarehouseEntityFactory.DOOR_LOCK_EXIST, warehouse.getDoorLockExist());
        assertEquals(WarehouseEntityFactory.AIR_CONDITIONING_TYPE, warehouse.getAirConditioningType());
        assertEquals(WarehouseEntityFactory.WORKER_EXIST, warehouse.getWorkerExist());
        assertEquals(WarehouseEntityFactory.CAN_PARK, warehouse.getCanPark());
        assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, warehouse.getWarehouseType());
        assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, warehouse.getMinReleasePerMonth());
        assertEquals(WarehouseEntityFactory.LATITUDE, warehouse.getLatitude());
        assertEquals(WarehouseEntityFactory.LONGITUDE, warehouse.getLongitude());
        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurance::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.INSURANCES)));
        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompany::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.SECURITY_COMPANIES)));
        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryType::getName).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES)));
        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseConditions::getCondition).collect(Collectors.toList()).containsAll(Arrays.asList(WarehouseEntityFactory.WAREHOUSE_CONDITIONS)));
        assertEquals(0, mainItemTypesRepository.findByWarehouseId(warehouseId).size());
        assertNull(warehouse.getBlogUrl());
        assertNull(warehouse.getMainImage());
        assertEquals(0, warehouse.getWarehouseImages().size());
    }

    @Test
    public void get_warehouseDetail_responseIsOk_IfUserIsOwner() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertWarehouseDetail(warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsOk_IfUserIsShipper() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertWarehouseDetail(warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfIsViewableIsRejected() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        Warehouses _warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseDetail_responseIsNotFound_IfWarehouseNotExist() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        String url = String.format("/v3/warehouses/%d", 0);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
