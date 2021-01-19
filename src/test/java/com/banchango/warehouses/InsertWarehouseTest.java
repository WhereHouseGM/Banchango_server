package com.banchango.warehouses;

import com.banchango.ApiTestContext;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.factory.request.WarehouseInsertRequestFactory;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class InsertWarehouseTest extends ApiTestContext {
    @Autowired
    private UserEntityFactory userEntityFactory;

    // POST API에 이메일 전송 기능이 추가되어, 아래 테스트 코드는 일단 주석 처리 하겠습니다.
//    @Test
//    public void post_warehouse_responseIsOk_IfAllConditionsAreRight() {
//        List<MainItemType> mainItemTypes = new ArrayList<>();
//        mainItemTypes.add(MainItemType.CLOTH);
//        mainItemTypes.add(MainItemType.BOOK);
//
//        List<WarehouseCondition> warehouseConditions = new ArrayList<>();
//        warehouseConditions.add(WarehouseCondition.LOW_TEMPERATURE);
//        warehouseConditions.add(WarehouseCondition.ROOM_TEMPERATURE);
//
//        List<String> strings = new ArrayList<>();
//        strings.add("one");
//        strings.add("two");
//        strings.add("three");
//
//        WarehouseInsertRequestDto requestBody = WarehouseInsertRequestDto.builder()
//            .name("TEST_NAME")
//            .space(123)
//            .address("address")
//            .addressDetail("addressDetail")
//            .description("description")
//            .availableWeekdays(1)
//            .openAt("06:00")
//            .closeAt("18:00")
//            .availableTimeDetail("availableTimeDetail")
//            .insurance("insurance")
//            .cctvExist(true)
//            .securityCompanyName("name")
//            .doorLockExist(true)
//            .airConditioningType(AirConditioningType.BOTH)
//            .workerExist(true)
//            .canPark(true)
//            .mainItemTypes(mainItemTypes)
//            .warehouseType(WarehouseType.THREEPL)
//            .minReleasePerMonth(23)
//            .latitude(22.2)
//            .longitude(22.2)
//            .deliveryTypes(strings)
//            .warehouseCondition(warehouseConditions)
//            .warehouseCondition(warehouseConditions)
//            .warehouseFacilityUsages(strings)
//            .warehouseUsageCautions(strings)
//            .build();
//
//
//        RequestEntity<WarehouseInsertRequestDto> request = RequestEntity.post(URI.create("/v3/warehouses"))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer " + accessToken)
//            .body(requestBody);
//
//        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("message"));
//    }

    @Test
    public void post_warehouse_responseIsForbidden_IfUserIsShipper() {
        Users shipper = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(shipper);
        WarehouseInsertRequestDto warehouseInsertRequestDto = WarehouseInsertRequestFactory.create();

        RequestEntity<WarehouseInsertRequestDto> request = RequestEntity.post("/v3/warehouses")
                .header("Authorization", "Bearer "+accessToken)
                .body(warehouseInsertRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
