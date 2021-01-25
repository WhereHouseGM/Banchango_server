package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.WarehouseInsertRequestResponseListDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.WarehouseStatus;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.*;

public class AdminGetWarehousesTest extends ApiIntegrationTest {

    @Test
    public void get_InProgressWarehouses_ResultIsNotFound_ifNotExist_and_AdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_InProgressWarehouses_ResultHasItems_ifExist_and_AdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

        ResponseEntity<WarehouseInsertRequestResponseListDto> response = restTemplate.exchange(request, WarehouseInsertRequestResponseListDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getRequests().size() > 0);
        assertNotNull(response.getBody().getRequests().get(0).getName());
        assertNotNull(response.getBody().getRequests().get(0).getWarehouseId());
        assertNotNull(response.getBody().getRequests().get(0).getLastModifiedAt());
    }

    @Test
    public void get_InProgressWarehouses_ResultHasItems_ifExist_and_AdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

        ResponseEntity<WarehouseInsertRequestResponseListDto> response = restTemplate.exchange(request, WarehouseInsertRequestResponseListDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getRequests().size() > 0);
        assertNotNull(response.getBody().getRequests().get(0).getName());
        assertNotNull(response.getBody().getRequests().get(0).getWarehouseId());
        assertNotNull(response.getBody().getRequests().get(0).getLastModifiedAt());
    }

    @Test
    public void get_AnyWarehouses_ResultHasItems_ifExist() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createRejectedWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

        ResponseEntity<WarehouseInsertRequestResponseListDto> response = restTemplate.exchange(request, WarehouseInsertRequestResponseListDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getRequests().size() == 4);
        assertNotNull(response.getBody().getRequests().get(0).getName());
        assertNotNull(response.getBody().getRequests().get(0).getWarehouseId());
        assertNotNull(response.getBody().getRequests().get(0).getLastModifiedAt());
    }

    @Test
    public void get_InProgressWarehouses_responseIsForbidden_IfTokenIsBad() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
