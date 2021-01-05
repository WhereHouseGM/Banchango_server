package com.banchango.estimates;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.*;
import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.estimates.dto.WarehouseEstimateItemInsertRequestDto;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.factory.request.EstimatesInsertRequestFactory;
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
import java.util.List;

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

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private WarehouseEntityFactory warehouseEntityFactory;

    String accessToken = null;
    Users user = null;

    @Before
    public void beforeTest() {
        estimatesRepository.deleteAll();
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();

        user = userEntityFactory.createUser();
        accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
    }

    @After
    public void afterTest() {
        estimatesRepository.deleteAll();
        usersRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    public void post_estimate_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

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
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

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
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

        WarehouseEstimateInsertRequestDto newWarehouseEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<WarehouseEstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(newWarehouseEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }


}