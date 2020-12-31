package com.banchango.warehouses;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.users.exception.UserEmailNotFoundException;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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
                    .companyName("companyName")
                    .build();
            usersRepository.save(user);

            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        }
        warehouseRepository.deleteAll();
    }

    @After
    public void afterTest() {
        if(user != null) {
            user = usersRepository.findByEmail("TEST_EMAIL1").orElseThrow(UserEmailNotFoundException::new);
            usersRepository.delete(user);
        }
        warehouseRepository.deleteAll();
    }

    @Test
    public void post_warehouse_responseIsOk_IfAllConditionsAreRight() {
        List<MainItemType> mainItemTypes = new ArrayList<>();
        mainItemTypes.add(MainItemType.CLOTH);
        mainItemTypes.add(MainItemType.BOOK);

        List<WarehouseCondition> warehouseConditions = new ArrayList<>();
        warehouseConditions.add(WarehouseCondition.LOW_TEMPERATURE);
        warehouseConditions.add(WarehouseCondition.ROOM_TEMPERATURE);

        List<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        WarehouseInsertRequestDto requestBody = WarehouseInsertRequestDto.builder()
            .name("TEST_NAME")
            .space(123)
            .address("address")
            .addressDetail("addressDetail")
            .description("description")
            .availableWeekdays(1)
            .openAt("06:00")
            .closeAt("18:00")
            .availableTimeDetail("availableTimeDetail")
            .insurance("insurance")
            .cctvExist(true)
            .securityCompanyName("name")
            .doorLockExist(true)
            .airConditioningType(AirConditioningType.BOTH)
            .workerExist(true)
            .canPark(true)
            .mainItemTypes(mainItemTypes)
            .warehouseType(WarehouseType.THREEPL)
            .minReleasePerMonth(23)
            .latitude(22.2)
            .longitude(22.2)
            .deliveryTypes(strings)
            .warehouseCondition(warehouseConditions)
            .warehouseCondition(warehouseConditions)
            .warehouseFacilityUsages(strings)
            .warehouseUsageCautions(strings)
            .build();


        RequestEntity<WarehouseInsertRequestDto> request = RequestEntity.post(URI.create("/v3/warehouses"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(requestBody);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("message"));
    }

    @Test
    public void delete_warehouse_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH });
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
        Warehouses tempWarehouse = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH });

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
        assertNotNull(warehouse.getMainItemTypes());

        for(WarehouseSearchDto _warehouse : warehouses) {
            String address = _warehouse.getAddress().toLowerCase();
            assertTrue(address.contains(addressQuery.toLowerCase()));
        }

        warehouseRepository.delete(tempWarehouse);
    }

    @Test
    public void get_warehouseByAddress_responseIsNoContent_IfIsViewableIsFalse() {
        Warehouses tempWarehouse = saveWarehouse(false, new MainItemType[] { MainItemType.CLOTH });

        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
        Warehouses tempWarehouse = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH });
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

        warehouseRepository.delete(tempWarehouse);
    }

    @Test
    public void get_warehouseForMain_responseIsNoContent_IfIsViewableIsFalse() {
        Warehouses tempWarehouse = saveWarehouse(false, new MainItemType[] { MainItemType.CLOTH });
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

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
        Warehouses warehouse1 = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH, MainItemType.COSMETIC });
        Warehouses warehouse2 = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH, MainItemType.ACCESSORY });
        Warehouses warehouse3 = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH, MainItemType.BOOK });

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
            // 매칭된 mainItemType의 match가 true인지 검사
            _warehouse.getMainItemTypes().stream()
                .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() == MainItemType.CLOTH || mainItemTypeMatchDto.getName() == MainItemType.COSMETIC)
                .forEach(mainItemTypeMatchDto -> assertTrue(mainItemTypeMatchDto.getMatch()));

            // 매칭되지 않은 mainItemType의 match가 false인지 검사
            _warehouse.getMainItemTypes().stream()
                .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() != MainItemType.CLOTH && mainItemTypeMatchDto.getName() != MainItemType.COSMETIC)
                .forEach(mainItemTypeMatchDto -> assertFalse(mainItemTypeMatchDto.getMatch()));
        }

        warehouseRepository.delete(warehouse1);
        warehouseRepository.delete(warehouse2);
        warehouseRepository.delete(warehouse3);
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNoContent_IfIsViewableIsFalse() {
        Warehouses warehouse = saveWarehouse(false, new MainItemType[] { MainItemType.CLOTH });

        String mainItemType = MainItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_warehouseByMainItemType_responseIsNoContent_IfWarehouseNotExist() {
        warehouseRepository.deleteAll();

        String mainItemType = MainItemType.CLOTH.toString();
        String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_warehouse_responseIsBadRequest_IfAddressAndMainItemTypeBothGiven() {
        warehouseRepository.deleteAll();

        String mainItemType = MainItemType.CLOTH.toString();
        String addressQuery = "addr";
        String url = String.format("/v3/warehouses?mainItemTypes=%s&address=%s&page=0&offset=5", mainItemType, addressQuery);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
            .build();

        ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void get_warehouseDetail_responseIsOk_IfAllConditionsAreRight() {
        Warehouses _warehouse = saveWarehouse(true, new MainItemType[] { MainItemType.CLOTH });
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
        assertNotNull(warehouse.getCanPark());
        assertNotNull(warehouse.getMainItemTypes());
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

        warehouseRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsForbidden_IfIsViewableIsFalse() {
        Warehouses _warehouse = saveWarehouse(false, new MainItemType[]{MainItemType.CLOTH});
        String url = String.format("/v3/warehouses/%d", _warehouse.getId());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        warehouseRepository.delete(_warehouse);
    }

    @Test
    public void get_warehouseDetail_responseIsNoContent_IfWarehouseNotExist() {
        String url = String.format("/v3/warehouses/%d", 0);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private Warehouses saveWarehouse(Boolean isViewableFlag, MainItemType[] mainItemTypes) {
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
                .cctvExist(true)
                .securityCompanyName("name")
                .doorLockExist(true)
                .airConditioningType(AirConditioningType.HEATING)
                .workerExist(true)
                .canPark(true)
                .warehouseType(WarehouseType.THREEPL)
                .minReleasePerMonth(2)
                .latitude(22.2)
                .longitude(22.2)
                .isViewableFlag(isViewableFlag)
                .build();

        List<MainItemTypes> m = Arrays.stream(mainItemTypes)
            .map(mainItemType -> new MainItemTypes(mainItemType, warehouse))
            .collect(Collectors.toList());

        warehouse.getMainItemTypes().addAll(m);

        return warehouseRepository.save(warehouse);
    }
}