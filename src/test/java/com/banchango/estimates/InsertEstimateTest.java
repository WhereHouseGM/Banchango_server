package com.banchango.estimates;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.factory.request.EstimatesInsertRequestFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InsertEstimateTest extends ApiIntegrationTest {

// 이메일 보내므로 200 테스트 불가능
//    @Test
//    public void post_estimate_responseIsOk_IfAllConditionsAreRight() {
//        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
//
//        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());
//
//        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer " + accessToken)
//            .body(newEstimateInsertRequestDto);
//
//        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody().getMessage());
//
//        warehousesRepository.delete(warehouse);
//    }

    @Test
    public void post_estimate_responseIsForbidden_IfUserIsOwner() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                .header("Authorization", "Bearer "+ownerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(newEstimateInsertRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void post_estimate_responseIsUnauthorized_IfAccessTokenNotGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(newEstimateInsertRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }


    @Test
    public void post_estimate_responseIsForbidden_IfWarehouseNotViewable() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouse warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(ownerAccessToken);

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + shipperAccessToken)
                .body(newEstimateInsertRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void post_estimate_responseIsNotFound_IfWarehouseNotExist() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Integer warehouseId = 0;
        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouseId);

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + shipperAccessToken)
                .body(newEstimateInsertRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }
}
