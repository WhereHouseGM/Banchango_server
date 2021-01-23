package com.banchango.estimateitems;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.EstimateItemEntityFactory;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EstimateItemsApiTest extends ApiTestContext {
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
    public void get_estimateItemsByestimateId_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + accessToken)
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimateItems());

        response.getBody().getEstimateItems()
            .forEach(estimateSearchDto -> {
                assertNotNull(estimateSearchDto.getId());
                assertEquals(estimateSearchDto.getName(), EstimateItemEntityFactory.NAME);
                assertEquals(estimateSearchDto.getKeepingNumber(), EstimateItemEntityFactory.KEEPING_NUMBER);
                assertEquals(estimateSearchDto.getWeight(), EstimateItemEntityFactory.WEIGHT);
                assertEquals(estimateSearchDto.getBarcode(), EstimateItemEntityFactory.BARCODE);
                assertEquals(estimateSearchDto.getSku(), EstimateItemEntityFactory.SKU);
                assertEquals(estimateSearchDto.getUrl(), EstimateItemEntityFactory.URL);
                assertEquals(estimateSearchDto.getPerimeter(), EstimateItemEntityFactory.PERIMETER);
                assertEquals(estimateSearchDto.getKeepingType(), EstimateItemEntityFactory.KEEPING_TYPE);
            });
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsNotFound_IfEstimateNotExist() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        final int estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimateId+"/items"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsNotFound_IfWarehouseStatusIsDeleted() {
        Warehouses warehouse = warehouseEntityFactory.createDeletedWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_estimateItemsByestimateId_responseIsForbidden_IfOtherUsersAccessTokenIsGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        String otherUsersAccessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                .header("Authorization", "Bearer " + otherUsersAccessToken)
                .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}