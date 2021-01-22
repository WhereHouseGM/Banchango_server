package com.banchango.estimateitems;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.factory.entity.EstimateItemEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetEstimateItemsTest extends ApiIntegrationTest {

    @Test
    public void get_estimateItemsByestimateId_responseIsOk_IfUserIsShipper() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Users shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + shipperAccessToken)
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimateItems());

        response.getBody().getEstimateItems().stream()
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
    public void get_estimateItemsByestimateId_responseIsNotFound_IfEstimateNotExist() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Users shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        final int estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimateId+"/items"))
                .header("Authorization", "Bearer " + shipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Users shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(ownerAccessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + shipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Users shipper = userEntityFactory.createUserWithShipperType();

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsForbidden_IfOtherUsersAccessTokenIsGiven() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Users shipper = userEntityFactory.createUserWithShipperType();
        Users otherShipper = userEntityFactory.createUserWithShipperType();
        String otherShipperAccessToken = JwtTokenUtil.generateAccessToken(otherShipper);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + otherShipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}