package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.EstimateStatusUpdateRequestDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.estimates.exception.EstimateNotFoundException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdminUpdateEstimateStatusTest extends ApiIntegrationTest {

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsOk_IfAdminIsOwner() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        estimate = estimateRepository.findById(estimate.getId()).orElseThrow(EstimateNotFoundException::new);

        assertEquals(EstimateStatus.IN_PROGRESS, estimate.getStatus());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsOk_IfAdminIsShipper() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        estimate = estimateRepository.findById(estimate.getId()).orElseThrow(EstimateNotFoundException::new);

        assertEquals(EstimateStatus.IN_PROGRESS, estimate.getStatus());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsUnAuthorized_IfTokenNotGiven() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
                .body(estimateStatusUpdateDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsForbidden_IfAccessTokenNotAdmin() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimate estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
                .header("Authorization", "Bearer " + accessToken)
                .body(estimateStatusUpdateDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsNotFound_IfEstimateNotFound() {
        User user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        User admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Warehouse warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);
        int estimateId = 0;

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimateId+"/status"))
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(estimateStatusUpdateDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
