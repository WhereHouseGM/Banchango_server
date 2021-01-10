package com.banchango.estimates;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.factory.entity.EstimateEntityFactory;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Autowired
    private EstimateEntityFactory estimateEntityFactory;

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

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(newEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void post_estimate_responseIsUnauthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(newEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void post_estimate_responseIsForbidden_IfWarehouseNotViewable() {
        Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

        EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

        RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(newEstimateInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        warehouseRepository.delete(warehouse);
    }

    @Test
    public void get_estimateByUserId_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + accessToken)
            .build();

        ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates().stream()
            .forEach(estimateSearchDto -> {
                assertNotNull(estimateSearchDto.getId());
                assertNotNull(estimateSearchDto.getWarehouse());
                assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                assertEquals(estimateSearchDto.getStatus(), EstimateStatus.RECEPTED);
            });
    }

    @Test
    public void get_estimateByUserId_responseIsNoContent_IfEstimatesNotExist() {
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + accessToken)
            .build();

        ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void get_estimateByUserId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
            .build();

        ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    public void get_estimateByUserId_responseIsForbidden_IfOtherUserId() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        String otherUsersAccessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
            .header("Authorization", "Bearer " + otherUsersAccessToken)
            .build();

        ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}