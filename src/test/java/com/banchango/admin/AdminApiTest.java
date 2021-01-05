package com.banchango.admin;

import com.banchango.ApiTestContext;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.*;
import com.banchango.factory.entity.WarehouseEntityFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class AdminApiTest extends ApiTestContext {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehousesRepository;

    @Autowired
    private WarehouseEntityFactory warehouseEntityFactory;

    Users user = null;
    String accessToken = null;
    Users admin = null;
    String adminAccessToken = null;

    @Before
    public void beforeTest() {
        if(user == null) {
            user = Users.builder()
                    .name("TEST_NAME")
                    .password("123")
                    .type(UserType.OWNER)
                    .email("TEST_EMAIL")
                    .phoneNumber("010123123")
                    .companyName("companyName")
                    .role(UserRole.USER)
                    .build();
            usersRepository.save(user);
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), UserRole.USER);
        }
        if(admin == null) {
            admin = Users.builder()
                    .name("ADMIN_NAME")
                    .password("!@#$")
                    .type(UserType.OWNER)
                    .email("ADMIN_EMAIL")
                    .phoneNumber("010234234")
                    .companyName("adminComp")
                    .role(UserRole.ADMIN)
                    .build();
            usersRepository.save(admin);
            adminAccessToken = JwtTokenUtil.generateAccessToken(admin.getUserId(), UserRole.ADMIN);
        }
        warehousesRepository.deleteAll();
    }

    @After
    public void afterTest() {
        warehousesRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    public void get_InProgressWarehouses_ResultIsNoContent_ifNotExist() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_InProgressWarehouses_ResultHasItems_ifExist() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JSONArray responseBody = new JSONArray(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.length() > 0);
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("warehouseId"));
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("name"));
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("createdAt"));
    }

    @Test
    public void get_InProgressWarehouses_responseIsForbidden_IfTokenIsBad() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsOk_ifAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d",warehouseId);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(request, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WarehouseEntityFactory.NAME, response.getBody().getName());
        assertEquals(WarehouseEntityFactory.SPACE, response.getBody().getSpace());
        assertEquals(WarehouseEntityFactory.ADDRESS, response.getBody().getAddress());
        assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, response.getBody().getAddressDetail());
        assertEquals(WarehouseEntityFactory.DESCRIPTION, response.getBody().getDescription());
        assertEquals(WarehouseEntityFactory.AVAILABLE_WEEKDAYS, response.getBody().getAvailableWeekdays());
        assertEquals(WarehouseEntityFactory.OPEN_AT, response.getBody().getOpenAt());
        assertEquals(WarehouseEntityFactory.CLOSE_AT, response.getBody().getCloseAt());
        assertEquals(WarehouseEntityFactory.AVAILABLE_TIME_DETAIL, response.getBody().getAvailableTimeDetail());
        assertEquals(WarehouseEntityFactory.CCTV_EXISTS, response.getBody().getCctvExist());
        assertEquals(WarehouseEntityFactory.DOOR_LOCK_EXIST, response.getBody().getDoorLockExist());
        assertEquals(WarehouseEntityFactory.AIR_CONDITIONING_TYPE, response.getBody().getAirConditioningType());
        assertEquals(WarehouseEntityFactory.WORKER_EXIST, response.getBody().getWorkerExist());
        assertEquals(WarehouseEntityFactory.CAN_PARK, response.getBody().getCanPark());
        assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, response.getBody().getWarehouseType());
        assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, response.getBody().getMinReleasePerMonth());
        assertEquals(WarehouseEntityFactory.LATITUDE, response.getBody().getLatitude());
        assertEquals(WarehouseEntityFactory.LONGITUDE, response.getBody().getLongitude());
        assertTrue(response.getBody().getDeliveryTypes().containsAll(Arrays.asList(WarehouseEntityFactory.DELIVERY_1, WarehouseEntityFactory.DELIVERY_2, WarehouseEntityFactory.DELIVERY_3)));
        assertTrue(response.getBody().getInsurances().containsAll(Arrays.asList(WarehouseEntityFactory.INSURANCE_1, WarehouseEntityFactory.INSURANCE_2, WarehouseEntityFactory.INSURANCE_3)));
        assertTrue(response.getBody().getSecurityCompanies().containsAll(Arrays.asList(WarehouseEntityFactory.SEC_COMP_1, WarehouseEntityFactory.SEC_COMP_2, WarehouseEntityFactory.SEC_COMP_3)));
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsNoContent_IfWarehouseNotExist() {
        String url = String.format("/v3/admin/warehouses/%d", 0);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsForbidden_IfTokenIsWrong() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
