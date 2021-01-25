package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.Warehouses;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DeleteWarehouseTest extends ApiIntegrationTest {

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsViewable() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsInProgress() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void delete_warehouse_responseIsOk_IfWarehouseStatusIsRejected() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createdRejectedWithNoMainItemTypes(accessToken);
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
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
        Users actualOwner = userEntityFactory.createUserWithOwnerType();
        Users owner = userEntityFactory.createUserWithOwnerType();
        String acutalOwnerAccessToken = JwtTokenUtil.generateAccessToken(actualOwner);
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(acutalOwnerAccessToken);

        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/"+warehouse.getId()))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void delete_warehouse_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);

        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/"+warehouse.getId()))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void delete_warehouse_responseIsNotFound_IfWarehouseNotExist() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/0"))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
