package com.banchango.warehouses.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.warehouses.dto.NewWarehouseRequestDto;
import com.banchango.warehouses.dto.SearchWarehouseDto;
import com.banchango.warehouses.dto.SearchWarehouseResponseDto;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WarehousesApiController {

    private final WarehousesService warehousesService;

    @ValidateRequired
    @PostMapping("/v2/warehouses")
    public BasicMessageResponseDto registerAgency(
            @Valid @RequestBody NewWarehouseRequestDto newWarehouseRequestDto,
            @RequestAttribute(name = "accessToken") String accessToken
    ) {
        warehousesService.saveAgencyWarehouse(newWarehouseRequestDto, accessToken);

        return new BasicMessageResponseDto("창고가 정상적으로 등록 되었습니다");
    }

    @GetMapping("/v2/warehouses")
    public SearchWarehouseResponseDto getAllWarehouses(
            @RequestParam(name = "address") String address,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset") Integer offset
    ) {
            List<SearchWarehouseDto> warehouses = warehousesService.search(address, limit, offset);

            return new SearchWarehouseResponseDto(warehouses);
    }
//
    @ValidateRequired
    @DeleteMapping("/v2/warehouses/{warehouseId}")
    public BasicMessageResponseDto delete(
            @PathVariable Integer warehouseId,
            @RequestAttribute(name = "accessToken") String accessToken
    ) {
        warehousesService.delete(warehouseId, accessToken);

        return new BasicMessageResponseDto("창고가 정상적으로 삭제되었습니다.");
    }
//
//    // DONE
//    @GetMapping("/v2/warehouses/agency/{mainItemType}")
//    public void getAgencyWarehouseList(HttpServletResponse response, @PathVariable String mainItemType) {
//        try {
//            WriteToClient.send(response, warehousesService.getAgencyWarehouseList(mainItemType), HttpServletResponse.SC_OK);
//        } catch(WarehouseNotFoundException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
//        } catch(Exception exception) {
//            exception.printStackTrace();
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//
//    // DONE
//    @GetMapping("/v2/warehouses/{warehouseId}")
//    public void getWarehouseById(@PathVariable Integer warehouseId, HttpServletResponse response) {
//        try {
//             WriteToClient.send(response, warehousesService.getSpecificWarehouseInfo(warehouseId), HttpServletResponse.SC_OK);
//        } catch(AuthenticateException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
//        } catch(WarehouseIdNotFoundException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/v2/warehouses/agency")
//    public void getList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletResponse response) {
//        try {
//            WriteToClient.send(response, warehousesService.getAgencyWarehouseList(page, size), HttpServletResponse.SC_OK);
//        } catch(WarehouseNotFoundException | WarehouseIdNotFoundException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//    // TODO : 창고 정보 수정 API
//    @PatchMapping("/v2/warehouses/{warehouseId}")
//    public void updateWarehouseInfo(@PathVariable Integer warehouseId, HttpServletResponse response) {
//
//    }
}
