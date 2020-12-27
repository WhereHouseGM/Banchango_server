package com.banchango.warehouses;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.*;
import com.banchango.users.exception.UserEmailNotFoundException;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WarehouseApiTest extends ApiTestContext {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehouseRepository;

    String accessToken = null;
    Users user = null;
    @Before
    public void beforeTest() {
        if(user == null) {
            user = Users.builder().name("TEST_NAME")
                    .email("TEST_EMAIL1")
                    .password("123")
                    .type(UserType.OWNER)
                    .phoneNumber("010123123")
                    .build();
            usersRepository.save(user);

            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        }
    }

    @After
    public void afterTest() {
        if(user != null) {
            user = usersRepository.findByEmail("TEST_EMAIL1").orElseThrow(UserEmailNotFoundException::new);
            usersRepository.delete(user);
        }
    }

    @Test
    public void post_warehouse_responseIsOk_IfAllConditionsAreRight() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "TEST_NAME");
        requestBody.put("space", 123);
        requestBody.put("address", "address");
        requestBody.put("addressDetail", "addressDetail");
        requestBody.put("description", "description");
        requestBody.put("availableWeekdays", 1);
        requestBody.put("openAt", "06:00");
        requestBody.put("closeAt", "18:00");
        requestBody.put("availableTimeDetail", "...");
        requestBody.put("insurance", "insurance");
        requestBody.put("cctvExist", 1);
        requestBody.put("securityCompanyName", "name");
        requestBody.put("doorLockExist", 1);
        requestBody.put("airConditioningType", AirConditioningType.HEATING);
        requestBody.put("workerExist", 1);
        requestBody.put("canPickup", 1);
        requestBody.put("canPark", 1);
        requestBody.put("mainItemType", "CLOTH");
        requestBody.put("warehouseType", "THREEPL");
        requestBody.put("minReleasePerMonth", 22);
        requestBody.put("latitude", 22.2);
        requestBody.put("longitude", 22.2);
        requestBody.put("deliveryTypes", new String[]{"one", "thow", "three"});
        requestBody.put("warehouseCondition", new String[]{"ROOM_TEMPERATURE", "LOW_TEMPERATURE"});
        requestBody.put("warehouseFacilityUsages", new String[]{"one", "thow", "three"});
        requestBody.put("warehouseUsageCautions", new String[]{"one", "thow", "three"});

        RequestEntity<String> request = RequestEntity.post(URI.create("/v3/warehouses"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+accessToken)
                .body(requestBody.toString());

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("message"));
    }

    @Test
    public void delete_warehouse_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = saveWarehouse();
        String url = "/v3/warehouses/"+warehouse.getId();

        RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void delete_warehouse_responseIsUnAuthorized_IfAuthorizationIsEmpty() {
        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/99999"))
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void delete_warehouse_responseIsNoContent_IfWarehouseNotExist() {
        RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/0"))
                .header("Authorization", "Bearer "+accessToken)
                .build();

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    public void get_warehouseByAddress_responseIsOk_IfAllConditionsAreRight() {
        Warehouses tempWarehouse = saveWarehouse();

        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
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
        assertNotNull(warehouse.getMainItemType());

        for(WarehouseSearchDto _warehouse : warehouses) {
            String address = _warehouse.getAddress().toLowerCase();
            assertTrue(address.contains(addressQuery.toLowerCase()));
        }

        warehouseRepository.delete(tempWarehouse);
    }

    @Test
    public void get_warehouseByAddress_responseIsNoContent_IfWarehouseNotExist() {
        warehouseRepository.deleteAll();
        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_warehouseForMain_responseIsOk_IfAllConditionsAreRight() {
        Warehouses tempWarehouse = saveWarehouse();
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
        assertNotNull(warehouse.getMainItemType());

        warehouseRepository.delete(tempWarehouse);
    }

    @Test
    public void get_warehouseForMain_responseIsNoContent_IfWarehouseNotExist() {
        warehouseRepository.deleteAll();
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_warehouseByMainItemType_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = saveWarehouse();

        String mainItemType = ItemTypeName.CLOTH.toString();
        String url = String.format("/v3/warehouses?category=%s&page=0&size=5", mainItemType);

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
        assertNotNull(warehouseSearchDto.getMainItemType());

        for(WarehouseSearchDto _warehouse : warehouses) {
            ItemTypeName _mainItemType = ItemTypeName.valueOf(mainItemType);
            assertEquals(_mainItemType, _warehouse.getMainItemType());
        }

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNoContent_IfWarehouseNotExist() {
        warehouseRepository.deleteByMainItemType(ItemTypeName.CLOTH);

        String mainItemType = ItemTypeName.CLOTH.toString();
        String url = String.format("/v3/warehouses?category=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_warehouse_responseIsBadRequest_IfAddressAndMainItemTypeBothGiven() {
        warehouseRepository.deleteByMainItemType(ItemTypeName.CLOTH);

        String mainItemType = ItemTypeName.CLOTH.toString();
        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?category=%s&address=%s&page=0&offset=5", mainItemType, addressQuery);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void get_warehouseDetail_responseIsOk_IfAllConditionsAreRight() {
        Warehouses _warehouse = saveWarehouse();
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        WarehouseDetailResponseDto warehouse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(warehouse.getWarehouseId());
        assertNotNull(warehouse.getOwnerId());
        assertNotNull(warehouse.getName());
        assertNotNull(warehouse.getSpace());
        assertNotNull(warehouse.getAddress());
        assertNotNull(warehouse.getAddressDetail());
        assertNotNull(warehouse.getDescription());
        assertNotNull(warehouse.getAvailableWeekdays());
        assertNotNull(warehouse.getOpenAt());
        assertNotNull(warehouse.getCloseAt());
        assertNotNull(warehouse.getAvailableTimeDetail());
        assertNotNull(warehouse.getInsurance());
        assertNotNull(warehouse.getCctvExist());
        assertNotNull(warehouse.getSecurityCompanyName());
        assertNotNull(warehouse.getDoorLockExist());
        assertNotNull(warehouse.getAirConditioningType());
        assertNotNull(warehouse.getWorkerExist());
        assertNotNull(warehouse.getCanPickup());
        assertNotNull(warehouse.getCanPark());
        assertNotNull(warehouse.getMainItemType());
        assertNotNull(warehouse.getWarehouseType());
        assertNotNull(warehouse.getMinReleasePerMonth());
        assertNotNull(warehouse.getLatitude());
        assertNotNull(warehouse.getLongitude());
        assertNotNull(warehouse.getMainImageUrl());
        assertNotNull(warehouse.getDeliveryTypes());
        assertNotNull(warehouse.getWarehouseCondition());
        assertNotNull(warehouse.getWarehouseFacilityUsages());
        assertNotNull(warehouse.getWarehouseUsageCautions());
        assertNotNull(warehouse.getImages());
    }

    @Test
    public void get_warehouseDetail_responseIsNoContent_IfWarehouseNotExist() {
        String url = String.format("/v3/warehouses/%d", 0);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        WarehouseDetailResponseDto warehouse = response.getBody();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private Warehouses saveWarehouse() {
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouses warehouse = Warehouses.builder()
                .userId(userId)
                .name("NAME")
                .space(123)
                .address("address")
                .addressDetail("addressDetail")
                .description("description")
                .availableWeekdays(1)
                .openAt("06:00")
                .closeAt("18:00")
                .availableTimeDetail("availableTimeDetail")
                .insurance("insurance")
                .cctvExist(1)
                .securityCompanyName("name")
                .doorLockExist(1)
                .airConditioningType(AirConditioningType.HEATING)
                .workerExist(1)
                .canPickup(1)
                .canPark(1)
                .mainItemType(ItemTypeName.CLOTH)
                .warehouseType(WarehouseType.THREEPL)
                .minReleasePerMonth(2)
                .latitude(22.2)
                .longitude(22.2)
                .build();

        return warehouseRepository.save(warehouse);
    }
}