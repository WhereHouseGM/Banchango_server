package com.banchango.admin;

import com.banchango.ApiIntegrationTest;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouses.AirConditioningType;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AdminUpdateWarehouseTest extends ApiIntegrationTest {

    private void assertUpdatedWarehouseInfo(Integer warehouseId, Boolean isBlogUrlNull) {
        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        assertEquals("NEW NAME", updatedWarehouse.getName());
        assertEquals(Integer.valueOf(999), updatedWarehouse.getSpace());
        assertEquals("NEW ADDRESS", updatedWarehouse.getAddress());
        assertEquals("NEW ADDR_DETAIL", updatedWarehouse.getAddressDetail());
        assertEquals("NEW DESC", updatedWarehouse.getDescription());
        assertEquals(Integer.valueOf(101010), updatedWarehouse.getAvailableWeekdays());
        assertEquals("08:00", updatedWarehouse.getOpenAt());
        assertEquals("23:30", updatedWarehouse.getCloseAt());
        assertEquals("NEW AVAIL_TIME_DETAIL", updatedWarehouse.getAvailableTimeDetail());
        assertFalse(updatedWarehouse.getCctvExist());
        assertFalse(updatedWarehouse.getDoorLockExist());
        assertEquals(AirConditioningType.NONE, updatedWarehouse.getAirConditioningType());
        assertFalse(updatedWarehouse.getWorkerExist());
        assertFalse(updatedWarehouse.getCanPark());
        assertTrue(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).containsAll(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS})));
        assertEquals(WarehouseType.FULFILLMENT, updatedWarehouse.getWarehouseType());
        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertEquals(WarehouseStatus.REJECTED, updatedWarehouse.getStatus());
        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseFacilityUsages::getContent).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"WH_FACILITY_USAGE"})));
        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseConditions::getCondition).collect(Collectors.toList()).containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        if(isBlogUrlNull) {
            assertNull(updatedWarehouse.getBlogUrl());
        } else {
            assertEquals("BLOG_URL", updatedWarehouse.getBlogUrl());
        }
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithoutBlogUrl_responseIsOk_ifAdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

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

        assertUpdatedWarehouseInfo(warehouseId, true);

//        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//        assertEquals("NEW NAME", updatedWarehouse.getName());
//        assertEquals(Integer.valueOf(999), updatedWarehouse.getSpace());
//        assertEquals("NEW ADDRESS", updatedWarehouse.getAddress());
//        assertEquals("NEW ADDR_DETAIL", updatedWarehouse.getAddressDetail());
//        assertEquals("NEW DESC", updatedWarehouse.getDescription());
//        assertEquals(Integer.valueOf(101010), updatedWarehouse.getAvailableWeekdays());
//        assertEquals("08:00", updatedWarehouse.getOpenAt());
//        assertEquals("23:30", updatedWarehouse.getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", updatedWarehouse.getAvailableTimeDetail());
//        assertFalse(updatedWarehouse.getCctvExist());
//        assertFalse(updatedWarehouse.getDoorLockExist());
//        assertEquals(AirConditioningType.NONE, updatedWarehouse.getAirConditioningType());
//        assertFalse(updatedWarehouse.getWorkerExist());
//        assertFalse(updatedWarehouse.getCanPark());
//        assertTrue(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).containsAll(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS})));
//        assertEquals(WarehouseType.FULFILLMENT, updatedWarehouse.getWarehouseType());
//        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertEquals(WarehouseStatus.REJECTED, updatedWarehouse.getStatus());
//        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new String[]{"WH_FACILITY_USAGE"})));
//        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals("BLOG_URL", updatedWarehouse.getBlogUrl());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithoutBlogUrl_responseIsOk_ifAdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

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

        assertUpdatedWarehouseInfo(warehouseId, true);

//        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//        assertEquals("NEW NAME", updatedWarehouse.getName());
//        assertEquals(Integer.valueOf(999), updatedWarehouse.getSpace());
//        assertEquals("NEW ADDRESS", updatedWarehouse.getAddress());
//        assertEquals("NEW ADDR_DETAIL", updatedWarehouse.getAddressDetail());
//        assertEquals("NEW DESC", updatedWarehouse.getDescription());
//        assertEquals(Integer.valueOf(101010), updatedWarehouse.getAvailableWeekdays());
//        assertEquals("08:00", updatedWarehouse.getOpenAt());
//        assertEquals("23:30", updatedWarehouse.getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", updatedWarehouse.getAvailableTimeDetail());
//        assertFalse(updatedWarehouse.getCctvExist());
//        assertFalse(updatedWarehouse.getDoorLockExist());
//        assertEquals(AirConditioningType.NONE, updatedWarehouse.getAirConditioningType());
//        assertFalse(updatedWarehouse.getWorkerExist());
//        assertFalse(updatedWarehouse.getCanPark());
//        assertTrue(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).containsAll(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS})));
//        assertEquals(WarehouseType.FULFILLMENT, updatedWarehouse.getWarehouseType());
//        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertEquals(WarehouseStatus.REJECTED, updatedWarehouse.getStatus());
//        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new String[]{"WH_FACILITY_USAGE"})));
//        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals("BLOG_URL", updatedWarehouse.getBlogUrl());

//        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
//                header("Authorization", "Bearer " + adminAccessToken).build();
//        ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
//        assertEquals("NEW NAME", secondResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", secondResponse.getBody().getOpenAt());
//        assertEquals("23:30", secondResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
//        assertFalse(secondResponse.getBody().getCctvExist());
//        assertFalse(secondResponse.getBody().getDoorLockExist());
//        assertFalse(secondResponse.getBody().getWorkerExist());
//        assertFalse(secondResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
//        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
//        assertNull(secondResponse.getBody().getBlogUrl());
//        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//        assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsOk_ifAdminIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

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
            .blogUrl("BLOG_URL")
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
        assertEquals("BLOG_URL", firstResponse.getBody().getBlogUrl());
        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

        assertUpdatedWarehouseInfo(warehouseId, false);

//        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//        assertEquals("NEW NAME", updatedWarehouse.getName());
//        assertEquals(Integer.valueOf(999), updatedWarehouse.getSpace());
//        assertEquals("NEW ADDRESS", updatedWarehouse.getAddress());
//        assertEquals("NEW ADDR_DETAIL", updatedWarehouse.getAddressDetail());
//        assertEquals("NEW DESC", updatedWarehouse.getDescription());
//        assertEquals(Integer.valueOf(101010), updatedWarehouse.getAvailableWeekdays());
//        assertEquals("08:00", updatedWarehouse.getOpenAt());
//        assertEquals("23:30", updatedWarehouse.getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", updatedWarehouse.getAvailableTimeDetail());
//        assertFalse(updatedWarehouse.getCctvExist());
//        assertFalse(updatedWarehouse.getDoorLockExist());
//        assertEquals(AirConditioningType.NONE, updatedWarehouse.getAirConditioningType());
//        assertFalse(updatedWarehouse.getWorkerExist());
//        assertFalse(updatedWarehouse.getCanPark());
//        assertTrue(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).containsAll(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS})));
//        assertEquals(WarehouseType.FULFILLMENT, updatedWarehouse.getWarehouseType());
//        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertEquals(WarehouseStatus.REJECTED, updatedWarehouse.getStatus());
//        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new String[]{"WH_FACILITY_USAGE"})));
//        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals("BLOG_URL", updatedWarehouse.getBlogUrl());

//        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
//            header("Authorization", "Bearer " + adminAccessToken).build();
//        ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
//        assertEquals("NEW NAME", secondResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", secondResponse.getBody().getOpenAt());
//        assertEquals("23:30", secondResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
//        assertFalse(secondResponse.getBody().getCctvExist());
//        assertFalse(secondResponse.getBody().getDoorLockExist());
//        assertFalse(secondResponse.getBody().getWorkerExist());
//        assertFalse(secondResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
//        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
//        assertEquals(BLOG_URL, secondResponse.getBody().getBlogUrl());
//        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//        assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsOk_ifAdminIsShipper() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithShipperType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

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
                .blogUrl("BLOG_URL")
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
        assertEquals("BLOG_URL", firstResponse.getBody().getBlogUrl());
        assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
        assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
        assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
        assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
        assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
        assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

        assertUpdatedWarehouseInfo(warehouseId, false);
//        Warehouses updatedWarehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//        assertEquals("NEW NAME", updatedWarehouse.getName());
//        assertEquals(Integer.valueOf(999), updatedWarehouse.getSpace());
//        assertEquals("NEW ADDRESS", updatedWarehouse.getAddress());
//        assertEquals("NEW ADDR_DETAIL", updatedWarehouse.getAddressDetail());
//        assertEquals("NEW DESC", updatedWarehouse.getDescription());
//        assertEquals(Integer.valueOf(101010), updatedWarehouse.getAvailableWeekdays());
//        assertEquals("08:00", updatedWarehouse.getOpenAt());
//        assertEquals("23:30", updatedWarehouse.getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", updatedWarehouse.getAvailableTimeDetail());
//        assertFalse(updatedWarehouse.getCctvExist());
//        assertFalse(updatedWarehouse.getDoorLockExist());
//        assertEquals(AirConditioningType.NONE, updatedWarehouse.getAirConditioningType());
//        assertFalse(updatedWarehouse.getWorkerExist());
//        assertFalse(updatedWarehouse.getCanPark());
//        assertTrue(mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).containsAll(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS})));
//        assertEquals(WarehouseType.FULFILLMENT, updatedWarehouse.getWarehouseType());
//        assertTrue(insurancesRepository.findByWarehouseId(warehouseId).stream().map(Insurances::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(securityCompaniesRepository.findByWarehouseId(warehouseId).stream().map(SecurityCompanies::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(deliveryTypesRepository.findByWarehouseId(warehouseId).stream().map(DeliveryTypes::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertEquals(WarehouseStatus.REJECTED, updatedWarehouse.getStatus());
//        assertTrue(warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new String[]{"WH_FACILITY_USAGE"})));
//        assertTrue(warehouseConditionsRepository.findByWarehouseId(warehouseId).containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertEquals("BLOG_URL", updatedWarehouse.getBlogUrl());

//        RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
//                header("Authorization", "Bearer " + adminAccessToken).build();
//        ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
//        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
//        assertEquals("NEW NAME", secondResponse.getBody().getName());
//        assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
//        assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
//        assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
//        assertEquals("NEW DESC", secondResponse.getBody().getDescription());
//        assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
//        assertEquals("08:00", secondResponse.getBody().getOpenAt());
//        assertEquals("23:30", secondResponse.getBody().getCloseAt());
//        assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
//        assertFalse(secondResponse.getBody().getCctvExist());
//        assertFalse(secondResponse.getBody().getDoorLockExist());
//        assertFalse(secondResponse.getBody().getWorkerExist());
//        assertFalse(secondResponse.getBody().getCanPark());
//        assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
//        assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
//        assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
//        assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
//        assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
//        assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
//        assertEquals(BLOG_URL, secondResponse.getBody().getBlogUrl());
//        assertTrue(secondResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
//        assertTrue(secondResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
//        assertTrue(secondResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
//        assertTrue(secondResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
//        assertTrue(secondResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));
//        assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsUnAuthorized_ifTokenNotGiven() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

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
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsForbidden_ifNotAdmin() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

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
                .header("Authorization", "Bearer "+accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void put_WarehouseInfoIsUpdatedWithBlogUrl_responseIsNotFound_ifWarehouseNotExist() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        Users admin = userEntityFactory.createAdminWithOwnerType();
        String adminAccessToken = JwtTokenUtil.generateAccessToken(admin);

        Integer warehouseId = 0;
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
                .blogUrl("BLOG_URL")
                .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                .status(WarehouseStatus.REJECTED)
                .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                .build();

        RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                .header("Authorization", "Bearer "+adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(putRequest, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
