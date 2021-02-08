package com.banchango.estimates;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.factory.entity.EstimateEntityFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetUserEstimatesTest extends ApiIntegrationTest {

    @Test
    public void get_estimateByUserId_responseIsOk_IfUserIsShipper() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+shipper.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + shipperAccessToken)
            .build();

        ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
            .forEach(estimateSearchDto -> {
                assertNotNull(estimateSearchDto.getId());
                assertNotNull(estimateSearchDto.getWarehouse());
                assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                assertEquals(estimateSearchDto.getStatus(), EstimateStatus.RECEPTED);
                assertEquals(EstimateEntityFactory.MONTHLY_AVERAGE_RELEASE, estimateSearchDto.getMonthlyAverageRelease());
            });
    }

    @Test
    public void get_estimateByUserId_responseIsForbidden_IfUserIsOwner() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), owner.getUserId());
        Estimate estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), owner.getUserId());
        Estimate estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), owner.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+owner.getUserId()+"/estimates"))
                .header("Authorization", "Bearer " + ownerAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_estimateByUserId_responseIsNotFound_IfEstimatesNotExist() {
        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+shipper.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + shipperAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateByUserId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+shipper.getUserId()+"/estimates"))
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    public void get_estimateByUserId_responseIsForbidden_IfOtherUserId() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String ownerAccessToken = JwtTokenUtil.generateAccessToken(owner);

        User shipper = userEntityFactory.createUserWithShipperType();
        String shipperAccessToken = JwtTokenUtil.generateAccessToken(shipper);

        User otherShipper = userEntityFactory.createUserWithShipperType();
        String otherShipperAccessToken = JwtTokenUtil.generateAccessToken(otherShipper);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(ownerAccessToken);
        Estimate estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());
        Estimate estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), shipper.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+shipper.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + otherShipperAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}