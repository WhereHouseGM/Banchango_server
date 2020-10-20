package com.banchango.warehouses.controller;

import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.dto.NewWarehouseFormDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

// TODO : JWT Token check
@RequiredArgsConstructor
@RestController
public class WarehousesApiController {

    private final WarehousesService warehousesService;

    @PostMapping("/v1/warehouses")
    public void register(@RequestBody NewWarehouseFormDto dto, HttpServletResponse response) {

        // TODO : NewWarehouseFormDto oneOf 부분
    }

    @GetMapping("/v1/delivery-types")
    public void getDeliveryTypes(HttpServletResponse response) {
        WriteToClient.send(response, warehousesService.getDeliveryTypes(), HttpServletResponse.SC_OK);
    }

    // TODO : warehouseId로 조회된 Entity가 없을 때 NOT FOUND ? 아니면 BAD REQUEST?
    @DeleteMapping("/v1/warehouses/{warehouseId}")
    public void delete(@PathVariable Integer warehouseId, HttpServletResponse response) {
        try {
            warehousesService.delete(warehouseId);
            WriteToClient.send(response, null, HttpServletResponse.SC_NO_CONTENT);
        } catch(WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
