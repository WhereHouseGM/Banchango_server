package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.factory.entity.EstimateItemEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdminGetEstimateItemsTest extends ApiIntegrationTest {

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsOk_IfUserIsOwner() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimateItems());

        response.getBody().getEstimateItems()
                .forEach(estimateSearchDto -> {
                    assertNotNull(estimateSearchDto.getId());
                    assertEquals(estimateSearchDto.getName(), EstimateItemEntityFactory.NAME);
                    assertEquals(estimateSearchDto.getKeepingNumber(), EstimateItemEntityFactory.KEEPING_NUMBER);
                    assertEquals(estimateSearchDto.getWeight(), EstimateItemEntityFactory.WEIGHT);
                    assertEquals(estimateSearchDto.getBarcode(), EstimateItemEntityFactory.BARCODE);
                    assertEquals(estimateSearchDto.getSku(), EstimateItemEntityFactory.SKU);
                    assertEquals(estimateSearchDto.getUrl(), EstimateItemEntityFactory.URL);
                    assertEquals(estimateSearchDto.getPerimeter(), EstimateItemEntityFactory.PERIMETER);
                    assertEquals(estimateSearchDto.getKeepingType(), EstimateItemEntityFactory.KEEPING_TYPE);
                });
    }

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsOk_IfUserIsShipper() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimateItems());

        response.getBody().getEstimateItems()
                .forEach(estimateSearchDto -> {
                    assertNotNull(estimateSearchDto.getId());
                    assertEquals(estimateSearchDto.getName(), EstimateItemEntityFactory.NAME);
                    assertEquals(estimateSearchDto.getKeepingNumber(), EstimateItemEntityFactory.KEEPING_NUMBER);
                    assertEquals(estimateSearchDto.getWeight(), EstimateItemEntityFactory.WEIGHT);
                    assertEquals(estimateSearchDto.getBarcode(), EstimateItemEntityFactory.BARCODE);
                    assertEquals(estimateSearchDto.getSku(), EstimateItemEntityFactory.SKU);
                    assertEquals(estimateSearchDto.getUrl(), EstimateItemEntityFactory.URL);
                    assertEquals(estimateSearchDto.getPerimeter(), EstimateItemEntityFactory.PERIMETER);
                    assertEquals(estimateSearchDto.getKeepingType(), EstimateItemEntityFactory.KEEPING_TYPE);
                });
    }

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsNotFound_IfEstimateNotExist() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        final int estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimateId+"/items"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsNotFound_IfEstimateItemsNotExist() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_adminEstimateItemsByEstimateId_responseIsForbidden_IfUserNotAdmin() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
