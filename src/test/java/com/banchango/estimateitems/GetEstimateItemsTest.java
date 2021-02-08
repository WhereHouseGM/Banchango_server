package com.banchango.estimateitems;

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

public class GetEstimateItemsTest extends ApiIntegrationTest {

    @Test
    public void get_estimateItemsByEstimateId_responseIsOk_IfUserIsShipper() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + shipperAccessToken)
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
    public void get_estimateItemsByEstimateId_responseIsNotFound_IfEstimateNotExist() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        final int estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimateId+"/items"))
                .header("Authorization", "Bearer " + shipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByEstimateId_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouse warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + shipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByEstimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByEstimateId_responseIsForbidden_IfOtherUsersAccessTokenIsGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        User otherShipper = userEntityFactory.createUserWithShipperType();
        String otherShipperAccessToken = JwtTokenUtil.generateAccessToken(otherShipper);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + otherShipperAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}