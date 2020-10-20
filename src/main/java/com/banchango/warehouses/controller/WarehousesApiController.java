package com.banchango.warehouses.controller;

import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.dto.NewWarehouseFormDto;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
