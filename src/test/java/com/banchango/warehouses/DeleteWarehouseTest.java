package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.*;

public class DeleteWarehouseTest extends ApiIntegrationTest {

    private void assertWarehouseDeleted(Integer warehouseId) {
        Warehouse warehouse = findWarehouseById.apply(warehouseId);
        assertEquals(WarehouseStatus.DELETED, warehouse.getStatus());
    }

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsViewable() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertWarehouseDeleted(warehouse.getId());
    }

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsInProgress() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertWarehouseDeleted(warehouse.getId());
    }

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsRejected() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertWarehouseDeleted(warehouse.getId());
    }

    @Test
    public void delete_warehouse_responseIsUnAuthorized_IfAuthorizationIsEmpty() {
        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/99999"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }


    @Test
    public void delete_warehouse_responseIsForbidden_IfNotMine() {
        User actualOwner = userEntityFactory.createUserWithOwnerType();
        User owner = userEntityFactory.createUserWithOwnerType();
        String acutalOwnerAccessToken = JwtTokenUtil.generateAccessToken(actualOwner);
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(acutalOwnerAccessToken);

        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/"+warehouse.getId()))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void delete_warehouse_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouse warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/"+warehouse.getId()))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void delete_warehouse_responseIsNotFound_IfWarehouseNotExist() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/0"))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
