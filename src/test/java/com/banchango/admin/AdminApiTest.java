package com.banchango.admin;

import com.banchango.ApiTestContext;
import com.banchango.admin.dto.WarehouseInsertRequestResponseDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.*;
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

    Users user = null;
    String accessToken = null;

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
                    .role(UserRole.ADMIN)
                    .build();
            usersRepository.save(user);
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), UserRole.ADMIN);
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
                .header("Authorization", "Bearer " + accessToken)
                .build();

        saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_InProgressWarehouses_ResultHasItems_ifExist() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JSONArray responseBody = new JSONArray(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.length() > 0);
        System.err.println(responseBody.get(0));
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("warehouseId"));
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("name"));
        assertNotNull(new JSONObject(responseBody.get(0).toString()).get("createdAt"));
    }

    @Test
    public void get_InProgressWarehouses_responseIsForbidden_IfTokenIsBad() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d", 0, 4);
        Users unAuthorizedUser = Users.builder()
                .name("TEST_NAME")
                .password("123")
                .type(UserType.OWNER)
                .email("TEST_EMAIL2")
                .phoneNumber("010123123")
                .companyName("companyName")
                .role(UserRole.USER)
                .build();
        Integer userId = usersRepository.save(unAuthorizedUser).getUserId();
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + JwtTokenUtil.generateAccessToken(userId, UserRole.USER))
                .build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private Warehouses saveWarehouse(WarehouseStatus status, MainItemType[] mainItemTypes) {
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
                .status(status)
                .build();

        List<MainItemTypes> m = Arrays.stream(mainItemTypes)
                .map(mainItemType -> new MainItemTypes(mainItemType, warehouse))
                .collect(Collectors.toList());

        warehouse.getMainItemTypes().addAll(m);

        return warehousesRepository.save(warehouse);
    }
}
