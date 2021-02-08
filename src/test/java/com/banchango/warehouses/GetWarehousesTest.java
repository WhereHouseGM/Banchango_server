package com.banchango.warehouses;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.users.User;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GetWarehousesTest extends ApiIntegrationTest {

    private void assertSearchResult(WarehouseSearchDto dto, Boolean isMainItemTypesNull) {
        assertEquals(WarehouseEntityFactory.NAME, dto.getName());
        assertEquals(WarehouseEntityFactory.SPACE, dto.getSpace());
        assertEquals(WarehouseEntityFactory.ADDRESS, dto.getAddress());
        assertEquals(WarehouseEntityFactory.OPEN_AT, dto.getOpenAt());
        assertEquals(WarehouseEntityFactory.CLOSE_AT, dto.getCloseAt());
        assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, dto.getWarehouseType());
        assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, dto.getMinReleasePerMonth());
        assertNotNull(dto.getMainImageUrl());
        assertTrue(dto.getDeliveryTypes().containsAll(Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES)));
        if(isMainItemTypesNull) {
            assertTrue(dto.getMainItemTypes().size() == 0);
        } else {
            assertTrue(dto.getMainItemTypes().size() != 0);
        }
    }

    @Test
    public void get_warehouseForMain_responseIsOk_IfAllConditionsAreRight() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses tempWarehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
        assertTrue(warehouses.size() > 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        warehouses.forEach(dto -> assertSearchResult(dto, true));
    }

    @Test
    public void get_warehouseForMain_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses tempWarehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseForMain_responseIsNotFound_IfWarehouseNotExist() {
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseByMainItemType_responseIsOk_IfAllConditionsAreRight() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);

        Warehouses warehouse1 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[] { ItemType.CLOTH, ItemType.COSMETIC });
        Warehouses warehouse2 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[] { ItemType.CLOTH, ItemType.ACCESSORY });
        Warehouses warehouse3 = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new ItemType[] { ItemType.CLOTH, ItemType.BOOK });

        String url = "/v3/warehouses?page=0&size=5&mainItemTypes=CLOTH,COSMETIC";

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
        assertTrue(warehouses.size() > 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        warehouses.forEach(dto -> assertSearchResult(dto, false));

        for (WarehouseSearchDto _warehouse : warehouses) {
            _warehouse.getMainItemTypes().stream()
                    .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() == ItemType.CLOTH || mainItemTypeMatchDto.getName() == ItemType.COSMETIC)
                    .forEach(mainItemTypeMatchDto -> assertTrue(mainItemTypeMatchDto.getMatch()));

            _warehouse.getMainItemTypes().stream()
                    .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() != ItemType.CLOTH && mainItemTypeMatchDto.getName() != ItemType.COSMETIC)
                    .forEach(mainItemTypeMatchDto -> assertFalse(mainItemTypeMatchDto.getMatch()));
        }
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNotFound_IfWarehouseStatusIsInProgress() {
        User owner = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(owner);
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new ItemType[] { ItemType.CLOTH });

        String mainItemType = ItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNotFound_IfWarehouseNotExist() {
        warehousesRepository.deleteAll();

        String mainItemType = ItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_warehouse_responseIsBadRequest_IfAddressAndMainItemTypeBothGiven() {
        warehousesRepository.deleteAll();

        String mainItemType = ItemType.CLOTH.toString();
        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?mainItemTypes=%s&address=%s&page=0&offset=5", mainItemType, addressQuery);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
