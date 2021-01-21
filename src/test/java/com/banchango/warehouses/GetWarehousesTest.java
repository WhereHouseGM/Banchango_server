package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

public class GetWarehousesTest extends ApiIntegrationTest {

    @Test
    public void get_warehouseForMain_responseIsOk_IfAllConditionsAreRight() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses tempWarehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
        assertTrue(warehouses.size() > 0);

        WarehouseSearchDto warehouse = warehouses.get(0);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        assertNotNull(warehouse.getAddress());
        assertNotNull(warehouse.getWarehouseId());
        assertNotNull(warehouse.getWarehouseCondition());
        assertNotNull(warehouse.getMinReleasePerMonth());
        assertNotNull(warehouse.getName());
        assertNotNull(warehouse.getWarehouseType());
        assertNotNull(warehouse.getCloseAt());
        assertNotNull(warehouse.getMainImageUrl());
        assertNotNull(warehouse.getOpenAt());
        assertNotNull(warehouse.getSpace());
        assertNotNull(warehouse.getDeliveryTypes());
        assertNotNull(warehouse.getMainItemTypes());
    }

    @Test
    public void get_warehouseForMain_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses tempWarehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseForMain_responseIsNotFound_IfWarehouseNotExist() {
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseByMainItemType_responseIsOk_IfAllConditionsAreRight() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse1 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[] { MainItemType.CLOTH, MainItemType.COSMETIC });
        Warehouses warehouse2 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[] { MainItemType.CLOTH, MainItemType.ACCESSORY });
        Warehouses warehouse3 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[] { MainItemType.CLOTH, MainItemType.BOOK });

        String url = "/v3/warehouses?page=0&size=5&mainItemTypes=CLOTH,COSMETIC";

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
        assertTrue(warehouses.size() > 0);

        WarehouseSearchDto warehouseSearchDto = warehouses.get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(warehouseSearchDto.getAddress());
        assertNotNull(warehouseSearchDto.getWarehouseId());
        assertNotNull(warehouseSearchDto.getWarehouseCondition());
        assertNotNull(warehouseSearchDto.getMinReleasePerMonth());
        assertNotNull(warehouseSearchDto.getName());
        assertNotNull(warehouseSearchDto.getWarehouseType());
        assertNotNull(warehouseSearchDto.getCloseAt());
        assertNotNull(warehouseSearchDto.getMainImageUrl());
        assertNotNull(warehouseSearchDto.getOpenAt());
        assertNotNull(warehouseSearchDto.getSpace());
        assertNotNull(warehouseSearchDto.getDeliveryTypes());
        assertNotNull(warehouseSearchDto.getMainItemTypes());

        for (WarehouseSearchDto _warehouse : warehouses) {
            _warehouse.getMainItemTypes().stream()
                    .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() == MainItemType.CLOTH || mainItemTypeMatchDto.getName() == MainItemType.COSMETIC)
                    .forEach(mainItemTypeMatchDto -> assertTrue(mainItemTypeMatchDto.getMatch()));

            _warehouse.getMainItemTypes().stream()
                    .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() != MainItemType.CLOTH && mainItemTypeMatchDto.getName() != MainItemType.COSMETIC)
                    .forEach(mainItemTypeMatchDto -> assertFalse(mainItemTypeMatchDto.getMatch()));
        }
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        Users owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[] { MainItemType.CLOTH });

        String mainItemType = MainItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNotFound_IfWarehouseNotExist() {
        warehousesRepository.deleteAll();

        String mainItemType = MainItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouse_responseIsBadRequest_IfAddressAndMainItemTypeBothGiven() {
        warehousesRepository.deleteAll();

        String mainItemType = MainItemType.CLOTH.toString();
        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?mainItemTypes=%s&address=%s&page=0&offset=5", mainItemType, addressQuery);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
