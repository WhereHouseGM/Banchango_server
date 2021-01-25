package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.EstimateDetailResponseDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.EstimateItemEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdminGetEstimateTest extends ApiIntegrationTest {

    @Test
    public void get_AdminEstimate_responseIsOk_IfAdminAndOwner() {
        Users adminOwner = userEntityFactory.createAdminWithOwnerType();
        String adminOwnerAccessToken = JwtTokenUtil.generateAccessToken(adminOwner);

        Users userOwner = userEntityFactory.createUserWithOwnerType();
        Users userShipper = userEntityFactory.createUserWithShipperType();
        String userOwnerAccessToken = JwtTokenUtil.generateAccessToken(userOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(userOwnerAccessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate1.getId()))
            .header("Authorization", "Bearer " + adminOwnerAccessToken)
            .build();

        ResponseEntity<EstimateDetailResponseDto> response = restTemplate.exchange(request, EstimateDetailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody().getId());
        assertEquals(EstimateEntityFactory.CONTENT, response.getBody().getContent());
        assertEquals(EstimateStatus.RECEPTED, response.getBody().getStatus());
        assertEquals(EstimateEntityFactory.MONTHLY_AVERAGE_RELEASE, response.getBody().getMonthlyAverageRelease());

        assertEquals(userShipper.getUserId(), response.getBody().getUser().getUserId());
        assertEquals(userShipper.getName(), response.getBody().getUser().getName());
        assertEquals(userShipper.getEmail(), response.getBody().getUser().getEmail());
        assertEquals(userShipper.getType(), response.getBody().getUser().getType());
        assertEquals(userShipper.getCompanyName(), response.getBody().getUser().getCompanyName());
        assertEquals(userShipper.getPhoneNumber(), response.getBody().getUser().getPhoneNumber());
        assertEquals(userShipper.getTelephoneNumber(), response.getBody().getUser().getTelephoneNumber());
        assertEquals(userShipper.getRole(), response.getBody().getUser().getRole());

        response.getBody().getItems().forEach(item -> {
            assertNotNull(item.getId());
            assertEquals(EstimateItemEntityFactory.NAME, item.getName());
            assertEquals(EstimateItemEntityFactory.KEEPING_NUMBER, item.getKeepingNumber());
            assertEquals(EstimateItemEntityFactory.WEIGHT, item.getWeight());
            assertEquals(EstimateItemEntityFactory.BARCODE, item.getBarcode());
            assertEquals(EstimateItemEntityFactory.SKU, item.getSku());
            assertEquals(EstimateItemEntityFactory.URL, item.getUrl());
            assertEquals(EstimateItemEntityFactory.PERIMETER, item.getPerimeter());
            assertEquals(EstimateItemEntityFactory.KEEPING_TYPE, item.getKeepingType());
        });

        assertEquals(warehouse.getName(), response.getBody().getWarehouseName());
    }

    @Test
    public void get_AdminEstimate_responseIsOk_IfAdminAndShipper() {
        Users adminShipper = userEntityFactory.createAdminWithShipperType();
        String adminShipperAccessToken = JwtTokenUtil.generateAccessToken(adminShipper);

        Users userOwner = userEntityFactory.createUserWithOwnerType();
        Users userShipper = userEntityFactory.createUserWithShipperType();
        String userOwnerAccessToken = JwtTokenUtil.generateAccessToken(userOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(userOwnerAccessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate1.getId()))
            .header("Authorization", "Bearer " + adminShipperAccessToken)
            .build();

        ResponseEntity<EstimateDetailResponseDto> response = restTemplate.exchange(request, EstimateDetailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody().getId());
        assertEquals(EstimateEntityFactory.CONTENT, response.getBody().getContent());
        assertEquals(EstimateStatus.RECEPTED, response.getBody().getStatus());
        assertEquals(EstimateEntityFactory.MONTHLY_AVERAGE_RELEASE, response.getBody().getMonthlyAverageRelease());

        assertEquals(userShipper.getUserId(), response.getBody().getUser().getUserId());
        assertEquals(userShipper.getName(), response.getBody().getUser().getName());
        assertEquals(userShipper.getEmail(), response.getBody().getUser().getEmail());
        assertEquals(userShipper.getType(), response.getBody().getUser().getType());
        assertEquals(userShipper.getCompanyName(), response.getBody().getUser().getCompanyName());
        assertEquals(userShipper.getPhoneNumber(), response.getBody().getUser().getPhoneNumber());
        assertEquals(userShipper.getTelephoneNumber(), response.getBody().getUser().getTelephoneNumber());
        assertEquals(userShipper.getRole(), response.getBody().getUser().getRole());

        response.getBody().getItems().forEach(item -> {
            assertNotNull(item.getId());
            assertEquals(EstimateItemEntityFactory.NAME, item.getName());
            assertEquals(EstimateItemEntityFactory.KEEPING_NUMBER, item.getKeepingNumber());
            assertEquals(EstimateItemEntityFactory.WEIGHT, item.getWeight());
            assertEquals(EstimateItemEntityFactory.BARCODE, item.getBarcode());
            assertEquals(EstimateItemEntityFactory.SKU, item.getSku());
            assertEquals(EstimateItemEntityFactory.URL, item.getUrl());
            assertEquals(EstimateItemEntityFactory.PERIMETER, item.getPerimeter());
            assertEquals(EstimateItemEntityFactory.KEEPING_TYPE, item.getKeepingType());
        });

        assertEquals(warehouse.getName(), response.getBody().getWarehouseName());
    }

    @Test
    public void get_AdminEstimate_responseIsUnAuthorized_IfTokenNotGiven() {
        Users userOwner = userEntityFactory.createUserWithOwnerType();
        Users userShipper = userEntityFactory.createUserWithShipperType();
        String userOwnerAccessToken = JwtTokenUtil.generateAccessToken(userOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(userOwnerAccessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate1.getId()))
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_AdminEstimate_responseIsForbidden_IfNotAdmin() {
        Users userOwner = userEntityFactory.createUserWithOwnerType();
        Users userShipper = userEntityFactory.createUserWithShipperType();
        String userOwnerAccessToken = JwtTokenUtil.generateAccessToken(userOwner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(userOwnerAccessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), userShipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate1.getId()))
            .header("Authorization", "Bearer " + userOwnerAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_AdminEstimate_responseIsNotFound_IfEstimateNotExist() {
        Users adminShipper = userEntityFactory.createAdminWithShipperType();
        String adminShipperAccessToken = JwtTokenUtil.generateAccessToken(adminShipper);

        Integer estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimateId))
            .header("Authorization", "Bearer " + adminShipperAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
