package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.warehouses.dto.MyWarehousesResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetMyWarehouseTest extends ApiIntegrationTest {

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusViewable_and_UserIsOwner() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses()
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
        User owner = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusInProgress() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses()
            .forEach(myWarehouse -> {
                assertNotNull(myWarehouse.getId());
                assertEquals(WarehouseEntityFactory.NAME, myWarehouse.getName());
                assertEquals(WarehouseEntityFactory.ADDRESS, myWarehouse.getAddress());
                assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, myWarehouse.getAddressDetail());
                assertEquals(WarehouseStatus.IN_PROGRESS, myWarehouse.getStatus());
                assertNotNull(myWarehouse.getMainImageUrl());
            });
    }

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusRejected() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getWarehouses());

        response.getBody().getWarehouses()
            .forEach(myWarehouse -> {
                assertNotNull(myWarehouse.getId());
                assertEquals(WarehouseEntityFactory.NAME, myWarehouse.getName());
                assertEquals(WarehouseEntityFactory.ADDRESS, myWarehouse.getAddress());
                assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, myWarehouse.getAddressDetail());
                assertEquals(WarehouseStatus.REJECTED, myWarehouse.getStatus());
                assertNotNull(myWarehouse.getMainImageUrl());
            });
    }

    @Test
    public void get_myWarehouses_responseIsUnauthorized_IfAccessTokenNotGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsForbidden_IfGivenOtherUserId() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        User otherUser = userEntityFactory.createUserWithOwnerType();

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+otherUser.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseNotExist() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseStatusDeleted() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfUserNotExist() {
        int invalidUserId = 0;
        String invalidUserAccessToken = JwtTokenUtil.generateAccessToken(invalidUserId, UserRole.USER, UserType.OWNER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+invalidUserId+"/warehouses"))
            .header("Authorization", "Bearer "+invalidUserAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}