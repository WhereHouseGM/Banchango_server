package com.banchango.warehouses.controller;

import com.banchango.warehouses.dto.NewWarehouseFormDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

// TODO : JWT Token check
@RestController
public class WarehousesApiController {

    @PostMapping("/v1/warehouses")
    public void register(@RequestBody NewWarehouseFormDto dto, HttpServletResponse response) {

        // TODO : NewWarehouseFormDto oneOf 부분
    }
}
