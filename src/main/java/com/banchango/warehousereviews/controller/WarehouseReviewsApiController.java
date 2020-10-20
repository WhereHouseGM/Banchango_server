package com.banchango.warehousereviews.controller;

import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehousereviews.dto.WarehouseReviewInsertRequestDto;
import com.banchango.warehousereviews.service.WarehouseReviewsService;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class WarehouseReviewsApiController {

    private final WarehouseReviewsService reviewsService;

    // TODO : Test 필요
    @GetMapping("/v1/warehouses/{warehouseId}/reviews")
    public void getReviewsOfWarehouse(@PathVariable Integer warehouseId, HttpServletResponse response,
                                      @RequestParam(name = "limit") Integer limit, @RequestParam(name = "offset") Integer offset) {
        try {
            WriteToClient.send(response, reviewsService.getWarehouseReviewsById(warehouseId, limit, offset), HttpServletResponse.SC_OK);
        } catch(WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // TODO : JWT에서 userId값 받아와서 저장하기
    @PostMapping("/v1/warehouses/{warehouseId}/reviews")
    public void register(@PathVariable Integer warehouseId, @RequestBody WarehouseReviewInsertRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, reviewsService.register(warehouseId, requestDto), HttpServletResponse.SC_OK);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
