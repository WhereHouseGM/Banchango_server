package com.banchango.estimates;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.*;
import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.estimates.dto.WarehouseEstimateItemInsertRequestDto;
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
public class EstimateApiTests extends ApiTestContext {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehouseRepository;

    @Autowired
    private EstimatesRepository estimatesRepository;

    String accessToken = null;
    Users user = null;
    @Before
    public void beforeTest() {
        estimatesRepository.deleteAll();
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();
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
    }

    @After
    public void afterTest() {
        estimatesRepository.deleteAll();
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    public void post_estimate_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] {MainItemType.CLOTH});

        List<WarehouseEstimateItemInsertRequestDto> estimateItemInsertRequestDtos = new ArrayList<>();
        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto0 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name0")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto1 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name1")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto0);
        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto1);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = WarehouseEstimateInsertRequestDto.builder()
            .warehouseId(warehouse.getId())
            .content("content")
            .estimateItems(estimateItemInsertRequestDtos)
            .build();

        RequestEntity<WarehouseEstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(newWarehouseEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void post_estimate_responseIsUnauthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] {MainItemType.CLOTH});

        List<WarehouseEstimateItemInsertRequestDto> estimateItemInsertRequestDtos = new ArrayList<>();
        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto0 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name0")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto1 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name1")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto0);
        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto1);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = WarehouseEstimateInsertRequestDto.builder()
            .warehouseId(warehouse.getId())
            .content("content")
            .estimateItems(estimateItemInsertRequestDtos)
            .build();

        RequestEntity<WarehouseEstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(newWarehouseEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void post_estimate_responseIsForbidden_IfWarehouseNotViewable() {
        Warehouses warehouse = saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[] {MainItemType.CLOTH});

        List<WarehouseEstimateItemInsertRequestDto> estimateItemInsertRequestDtos = new ArrayList<>();
        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto0 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name0")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        WarehouseEstimateItemInsertRequestDto warehouseEstimateInsertRequestDto1 = WarehouseEstimateItemInsertRequestDto.builder()
            .name("name1")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();

        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto0);
        estimateItemInsertRequestDtos.add(warehouseEstimateInsertRequestDto1);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = WarehouseEstimateInsertRequestDto.builder()
            .warehouseId(warehouse.getId())
            .content("content")
            .estimateItems(estimateItemInsertRequestDtos)
            .build();

        RequestEntity<WarehouseEstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(newWarehouseEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
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

        return warehouseRepository.save(warehouse);
    }
}