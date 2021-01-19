package com.banchango.warehouses;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.*;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.factory.request.WarehouseInsertRequestFactory;
import com.banchango.users.exception.UserEmailNotFoundException;
import com.banchango.warehouses.dto.*;
import com.sun.mail.iap.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GetMyWarehouseTest extends ApiTestContext {

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private WarehousesRepository warehouseRepository;

    @Autowired
    private WarehouseEntityFactory warehouseEntityFactory;

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusViewable() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
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

        warehouseRepository.delete(warehouse);
    }


    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusInProgress() {
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
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

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsOk_IfWarehouseStatusRejected() {
        Warehouses warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
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

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsUnauthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsForbidden_IfGivenOtherUserId() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Users otherUser = userEntityFactory.createUser();

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+otherUser.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseNotExist() {
        warehouseRepository.deleteAll();

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfWarehouseStatusDeleted() {
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/warehouses"))
            .header("Authorization", "Bearer "+accessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_myWarehouses_responseIsNotFound_IfUserNotExist() {
        int invalidUserId = 0;
        String invalidUserAccessToken = JwtTokenUtil.generateAccessToken(invalidUserId, UserRole.USER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+invalidUserId+"/warehouses"))
            .header("Authorization", "Bearer "+invalidUserAccessToken)
            .build();

        ResponseEntity<MyWarehousesResponseDto> response = restTemplate.exchange(request, MyWarehousesResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}