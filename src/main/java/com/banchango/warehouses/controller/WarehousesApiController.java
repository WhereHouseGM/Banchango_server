package com.banchango.warehouses.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.dto.AgencyWarehouseInsertRequestDto;
import com.banchango.warehouses.exception.WarehouseAlreadyRegisteredException;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import com.banchango.warehouses.exception.WarehouseSearchException;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class WarehousesApiController {

    private final WarehousesService warehousesService;

    @PostMapping("/v2/warehouses/agency")
    public void register(@RequestBody AgencyWarehouseInsertRequestDto dto,
                         @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            WriteToClient.send(response, warehousesService.save(dto, bearerToken), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseAlreadyRegisteredException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /*
    // TODO : JWT Token Test
    @GetMapping("/v2/delivery-types")
    public void getDeliveryTypes(@RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            WriteToClient.send(response, warehousesService.getDeliveryTypes(bearerToken), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

     */

    // DONE
    @GetMapping("/v2/warehouses")
    public void getAllWarehouses(@RequestParam(name = "address") String address,
                                 @RequestParam(name = "limit") Integer limit,
                                 @RequestParam(name = "offset") Integer offset,
                                 HttpServletResponse response) {
        try {
            WriteToClient.send(response, warehousesService.search(address, limit, offset), HttpServletResponse.SC_OK);
        } catch(WarehouseSearchException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @DeleteMapping("/v2/warehouses/{warehouseId}")
    public void delete(@PathVariable Integer warehouseId, @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            WriteToClient.send(response, warehousesService.delete(warehouseId, bearerToken), HttpServletResponse.SC_OK);
        } catch(WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(Exception exception) {
            exception.printStackTrace();
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // TODO : 특정 창고 정보 조회 API
    @GetMapping("/v2/warehouses/{warehouseId}")
    public void getWarehouseById(@PathVariable Integer warehouseId, HttpServletResponse response) {

    }

    // TODO : 창고 정보 수정 API
    @PatchMapping("/v2/warehouses/{warehouseId}")
    public void updateWarehouseInfo(@PathVariable Integer warehouseId, HttpServletResponse response) {

    }
}
