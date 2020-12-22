package com.banchango.warehouses;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.users.exception.UserEmailNotFoundException;
import org.json.JSONObject;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarehouseApiTest extends ApiTestContext {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehouseRepository;

    String accessToken = null;
    List<Integer> warehouseIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        Users user = Users.builder().name("TEST_NAME")
                .email("TEST_EMAIL1")
                .password("123")
                .type(UserType.OWNER)
                .telephoneNumber("02123123")
                .phoneNumber("010123123")
                .companyName("TEST_COMP")
                .build();
        usersRepository.save(user);

        accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
    }

    @After
    public void afterTest() {
        Users user = usersRepository.findByEmail("TEST_EMAIL1").orElseThrow(UserEmailNotFoundException::new);
        usersRepository.delete(user);

        for(Integer warehouseId : warehouseIds) warehouseRepository.delete_(warehouseId);
    }

    @Test
    public void warehouse_responseIsOk_IfAllConditionsAreRight() {
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

        RequestEntity<String> request = RequestEntity.post(URI.create("/v2/warehouses"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+accessToken)
                .body(requestBody.toString());

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("message"));
    }
}
