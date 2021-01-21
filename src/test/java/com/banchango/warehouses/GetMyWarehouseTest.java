package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.*;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.warehouses.dto.*;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.*;

public class GetMyWarehouseTest extends ApiIntegrationTest {

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusViewable_and_UserIsOwner() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses().stream()
            .forEach(myWarehouse -> {
                assertNotNull(myWarehouse.getId());
                assertEquals(WarehouseEntityFactory.NAME, myWarehouse.getName());
                assertEquals(WarehouseEntityFactory.ADDRESS, myWarehouse.getAddress());
                assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, myWarehouse.getAddressDetail());
                assertEquals(WarehouseStatus.VIEWABLE, myWarehouse.getStatus());
                assertNotNull(myWarehouse.getMainImageUrl());
            });
    }

    @Test
    public void get_myWarehouses_responseIsForbidden_IfWarehouseStatusViewable_and_UserIsShipper() {
        Users owner = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusInProgress() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses().stream()
            .forEach(myWarehouse -> {
                assertNotNull(myWarehouse.getId());
                assertEquals(WarehouseEntityFactory.NAME, myWarehouse.getName());
                assertEquals(WarehouseEntityFactory.ADDRESS, myWarehouse.getAddress());
                assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, myWarehouse.getAddressDetail());
                assertEquals(WarehouseStatus.IN_PROGRESS, myWarehouse.getStatus());
                assertNotNull(myWarehouse.getMainImageUrl());
            });

        warehousesRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusRejected() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses().stream()
            .forEach(myWarehouse -> {
                assertNotNull(myWarehouse.getId());
                assertEquals(WarehouseEntityFactory.NAME, myWarehouse.getName());
                assertEquals(WarehouseEntityFactory.ADDRESS, myWarehouse.getAddress());
                assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, myWarehouse.getAddressDetail());
                assertEquals(WarehouseStatus.REJECTED, myWarehouse.getStatus());
                assertNotNull(myWarehouse.getMainImageUrl());
            });

        warehousesRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsUnauthorized_IfAccessTokenNotGiven() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        warehousesRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsForbidden_IfGivenOtherUserId() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Users otherUser = userEntityFactory.createUserWithOwnerType();

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+otherUser.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        warehousesRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseNotExist() {
        warehousesRepository.deleteAll();

        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseStatusDeleted() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        warehousesRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfUserNotExist() {
        int invalidUserId = 0;
        String invalidUserAccessToken = JwtTokenUtil.generateAccessToken(invalidUserId, UserRole.USER, UserType.OWNER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+invalidUserId+"/warehouses"))
            .header("Authorization", "Bearer "+invalidUserAccessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}