package com.banchango.warehousereviews.controller;

import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehousereviews.service.WarehouseReviewsService;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
