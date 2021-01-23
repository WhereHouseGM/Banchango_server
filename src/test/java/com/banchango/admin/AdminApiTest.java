package com.banchango.admin;

import com.banchango.ApiTestContext;
import com.banchango.admin.dto.*;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.*;
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
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;


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
    private EstimatesRepository estimatesRepository;

    @Autowired
    private WarehouseEntityFactory warehouseEntityFactory;

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private EstimateEntityFactory estimateEntityFactory;

    Users user = null;
    String accessToken = null;
    Users admin = null;
    String adminAccessToken = null;

    @Before
    public void beforeTest() {
        if(user == null) {
            user = userEntityFactory.createUser();
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), UserRole.USER);
        }
        if(admin == null) {
            admin = userEntityFactory.createAdmin();
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
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
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
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());

        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();

        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
        warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
        warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

        ResponseEntity<WarehouseInsertRequestResponseListDto> response = restTemplate.exchange(request, WarehouseInsertRequestResponseListDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getRequests().size() > 0);
        assertNotNull(response.getBody().getRequests().get(0).getName());
        assertNotNull(response.getBody().getRequests().get(0).getWarehouseId());
        assertNotNull(response.getBody().getRequests().get(0).getCreatedAt());
    }

    @Test
    public void get_InProgressWarehouses_responseIsForbidden_IfTokenIsBad() {
        String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
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
        assertEquals(response.getBody().getDeliveryTypes(), Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES));
        assertEquals(response.getBody().getInsurances(), Arrays.asList(WarehouseEntityFactory.INSURANCES));
        assertEquals(response.getBody().getSecurityCompanies(), Arrays.asList(WarehouseEntityFactory.SECURITY_COMPANIES));
        assertNotNull(response.getBody().getCreatedAt());
        assertEquals(response.getBody().getWarehouseCondition(), Arrays.asList(WarehouseEntityFactory.WAREHOUSE_CONDITIONS));
        assertEquals(WarehouseStatus.VIEWABLE, response.getBody().getStatus());
        assertNull(response.getBody().getBlogUrl());
    }

    @Test
    public void get_AllInfosOfSpecificWarehouse_responseIsNotFound_IfWarehouseNotExist() {
        String url = String.format("/v3/admin/warehouses/%d", 0);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .header("Authorization", "Bearer " + adminAccessToken)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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

    @Test
    public void put_WarehouseInfoIsUpdatedWithoutBlogUrl_ifAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        WarehouseAdminUpdateRequestDto body = WarehouseAdminUpdateRequestDto.builder()
                .name("NEW NAME")
                .space(999)
                .address("NEW ADDRESS")
                .addressDetail("NEW ADDR_DETAIL")
                .description("NEW DESC")
                .availableWeekdays(101010)
                .openAt("08:00")
                .closeAt("23:30")
                .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                .cctvExist(false)
                .doorLockExist(false)
                .airConditioningType(AirConditioningType.NONE)
                .workerExist(false)
                .canPark(false)
                .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
                .warehouseType(WarehouseType.FULFILLMENT)
                .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                .minReleasePerMonth(101)
                .latitude(11.11)
                .longitude(33.33)
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .status(WarehouseStatus.REJECTED)
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();
        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminAccessToken)
                .body(body);

        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
        assertEquals("NEW NAME", firstResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", firstResponse.getBody().getOpenAt());
        assertEquals("23:30", firstResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
        assertFalse(firstResponse.getBody().getCctvExist());
        assertFalse(firstResponse.getBody().getDoorLockExist());
        assertFalse(firstResponse.getBody().getWorkerExist());
        assertFalse(firstResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
        assertNull(firstResponse.getBody().getBlogUrl());
        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
                header("Authorization", "Bearer " + adminAccessToken).build();
        ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals("NEW NAME", secondResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", secondResponse.getBody().getOpenAt());
        assertEquals("23:30", secondResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
        assertFalse(secondResponse.getBody().getCctvExist());
        assertFalse(secondResponse.getBody().getDoorLockExist());
        assertFalse(secondResponse.getBody().getWorkerExist());
        assertFalse(secondResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
        assertNull(secondResponse.getBody().getBlogUrl());
        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
        assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_ifAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
        Integer warehouseId = warehouse.getId();
        String url = String.format("/v3/admin/warehouses/%d", warehouseId);
        String BLOG_URL = "BLOG_URL";

        WarehouseAdminUpdateRequestDto body = WarehouseAdminUpdateRequestDto.builder()
            .name("NEW NAME")
            .space(999)
            .address("NEW ADDRESS")
            .addressDetail("NEW ADDR_DETAIL")
            .description("NEW DESC")
            .availableWeekdays(101010)
            .openAt("08:00")
            .closeAt("23:30")
            .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
            .cctvExist(false)
            .doorLockExist(false)
            .airConditioningType(AirConditioningType.NONE)
            .workerExist(false)
            .canPark(false)
            .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
            .warehouseType(WarehouseType.FULFILLMENT)
            .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
            .minReleasePerMonth(101)
            .latitude(11.11)
            .longitude(33.33)
            .blogUrl(BLOG_URL)
            .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
            .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
            .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
            .status(WarehouseStatus.REJECTED)
            .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
            .build();

        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + adminAccessToken)
            .body(body);

        ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
        assertEquals("NEW NAME", firstResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", firstResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", firstResponse.getBody().getOpenAt());
        assertEquals("23:30", firstResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
        assertFalse(firstResponse.getBody().getCctvExist());
        assertFalse(firstResponse.getBody().getDoorLockExist());
        assertFalse(firstResponse.getBody().getWorkerExist());
        assertFalse(firstResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
        assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
        assertEquals(BLOG_URL, firstResponse.getBody().getBlogUrl());
        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
            header("Authorization", "Bearer " + adminAccessToken).build();
        ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals("NEW NAME", secondResponse.getBody().getName());
        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
        assertEquals("08:00", secondResponse.getBody().getOpenAt());
        assertEquals("23:30", secondResponse.getBody().getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
        assertFalse(secondResponse.getBody().getCctvExist());
        assertFalse(secondResponse.getBody().getDoorLockExist());
        assertFalse(secondResponse.getBody().getWorkerExist());
        assertFalse(secondResponse.getBody().getCanPark());
        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
        assertEquals(BLOG_URL, secondResponse.getBody().getBlogUrl());
        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
        assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
    }

    @Test
    public void get_adminAllEstimates_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
            .forEach(summaryDto -> {
                assertNotNull(summaryDto.getStatus());
                assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                assertEquals(summaryDto.getName(), warehouse.getName());
                assertNotNull(summaryDto.getCreatedAt());
            });
    }

    @Test
    public void get_adminReceptedEstimates_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=RECEPTED"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
            .forEach(summaryDto -> {
                assertNotNull(summaryDto.getStatus());
                assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                assertEquals(summaryDto.getName(), warehouse.getName());
                assertNotNull(summaryDto.getCreatedAt());
            });
    }

    @Test
    public void get_adminInProgressEstimates_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=IN_PROGRESS"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
            .forEach(summaryDto -> {
                assertNotNull(summaryDto.getStatus());
                assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                assertEquals(summaryDto.getName(), warehouse.getName());
                assertNotNull(summaryDto.getCreatedAt());
            });
    }

    @Test
    public void get_adminDoneEstimates_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=DONE"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateSummaryListDto> response = restTemplate.exchange(request, EstimateSummaryListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEstimates());

        response.getBody().getEstimates()
            .forEach(summaryDto -> {
                assertNotNull(summaryDto.getStatus());
                assertEquals(summaryDto.getWarehouseId(), warehouse.getId());
                assertEquals(summaryDto.getName(), warehouse.getName());
                assertNotNull(summaryDto.getCreatedAt());
            });
    }

    @Test
    public void get_adminAllEstimates_responseIsNotFound_IfEstimatesNotExist() {
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_adminAllEstimates_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_adminAllEstimates_responseIsForbidden_IfNotAdmin() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
        Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
            .header("Authorization", "Bearer " + accessToken)
            .build();

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        estimate = estimatesRepository.findById(estimate.getId()).get();

        assertEquals(EstimateStatus.IN_PROGRESS, estimate.getStatus());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsUnAuthorized_IfTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
            .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsForbidden_IfAccessTokenNotAdmin() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimate.getId()+"/status"))
            .header("Authorization", "Bearer " + accessToken)
            .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void patch_adminUpdateEstimateStatus_responseIsNotFound_IfEstimateNotFound() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        EstimateStatusUpdateRequestDto estimateStatusUpdateDto = new EstimateStatusUpdateRequestDto(EstimateStatus.IN_PROGRESS);
        int estimateId = 0;

        RequestEntity<EstimateStatusUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/admin/estimates/"+estimateId+"/status"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .body(estimateStatusUpdateDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_adminEstimateItemsByestimateId_responseIsOk_IfAllConditionsAreRight() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + adminAccessToken)
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
    public void get_adminEstimateItemsByestimateId_responseIsNotFound_IfEstimateNotExist() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        final int estimateId = 0;

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimateId+"/items"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_get_adminEstimateItemsByestimateId_responseIsNotFound_IfEstimateItemsNotExist() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + adminAccessToken)
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_get_adminEstimateItemsByestimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void get_adminEstimateItemsByestimateId_responseIsForbidden_IfUserNotAdmin() {
        Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
        Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates/"+estimate.getId()+"/items"))
            .header("Authorization", "Bearer " + accessToken)
            .build();

        ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
