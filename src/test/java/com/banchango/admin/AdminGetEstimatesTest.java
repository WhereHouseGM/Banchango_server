package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.EstimateSummaryListDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.users.Users;
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

public class AdminGetEstimatesTest extends ApiIntegrationTest {

    @Test
    public void get_adminAllEstimates_responseIsOk_IfAdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
                .forEach(summaryDto -> {
                    assertNotNull(summaryDto.getId());
                    assertNotNull(summaryDto.getStatus());
                    assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                    assertEquals(summaryDto.getName(), warehouse.getName());
                    assertNotNull(summaryDto.getCreatedAt());
                });
    }

    @Test
    public void get_adminAllEstimates_responseIsOk_IfAdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
                .forEach(summaryDto -> {
                    assertNotNull(summaryDto.getId());
                    assertNotNull(summaryDto.getStatus());
                    assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                    assertEquals(summaryDto.getName(), warehouse.getName());
                    assertNotNull(summaryDto.getCreatedAt());
                });
    }

    @Test
    public void get_adminReceptedEstimates_responseIsOk_IfAllConditionsAreRight() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=RECEPTED"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
                .forEach(summaryDto -> {
                    assertNotNull(summaryDto.getId());
                    assertNotNull(summaryDto.getStatus());
                    assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                    assertEquals(summaryDto.getName(), warehouse.getName());
                    assertNotNull(summaryDto.getCreatedAt());
                });
    }

    @Test
    public void get_adminInProgressEstimates_responseIsOk_IfAllConditionsAreRight() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=IN_PROGRESS"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
                .forEach(summaryDto -> {
                    assertNotNull(summaryDto.getId());
                    assertNotNull(summaryDto.getStatus());
                    assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                    assertEquals(summaryDto.getName(), warehouse.getName());
                    assertNotNull(summaryDto.getCreatedAt());
                });
    }

    @Test
    public void get_adminDoneEstimates_responseIsOk_IfAllConditionsAreRight() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=DONE"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
                .forEach(summaryDto -> {
                    assertNotNull(summaryDto.getId());
                    assertNotNull(summaryDto.getStatus());
                    assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                    assertEquals(summaryDto.getName(), warehouse.getName());
                    assertNotNull(summaryDto.getCreatedAt());
                });
    }

    @Test
    public void get_adminAllEstimates_responseIsNotFound_IfEstimatesNotExist() {
        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_adminAllEstimates_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_adminAllEstimates_responseIsForbidden_IfNotAdmin() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
