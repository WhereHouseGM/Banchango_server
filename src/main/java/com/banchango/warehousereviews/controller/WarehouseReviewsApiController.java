package com.banchango.warehousereviews.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehousereviews.dto.WarehouseReviewInsertRequestDto;
import com.banchango.warehousereviews.exception.WarehouseReviewInvalidAccessException;
import com.banchango.warehousereviews.exception.WarehouseReviewNotFoundException;
import com.banchango.warehousereviews.service.WarehouseReviewsService;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class WarehouseReviewsApiController {

    private final WarehouseReviewsService reviewsService;


    // DONE
    @GetMapping("/v2/warehouses/{warehouseId}/reviews")
    public void getReviewsOfWarehouse(@PathVariable Integer warehouseId, HttpServletResponse response, @RequestHeader(name = "Authorization") String bearerToken,
                                      @RequestParam(name = "limit") Integer limit, @RequestParam(name = "offset") Integer offset) {
        try {
            WriteToClient.send(response, reviewsService.getWarehouseReviewsById(warehouseId, limit, offset, bearerToken), HttpServletResponse.SC_OK);
        } catch(WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseReviewNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @PostMapping("/v2/warehouses/{warehouseId}/reviews")
    public void register(@PathVariable Integer warehouseId, @RequestHeader(name = "Authorization") String bearerToken,
                         @RequestBody WarehouseReviewInsertRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, reviewsService.register(warehouseId, requestDto, bearerToken), HttpServletResponse.SC_OK);
        } catch(WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @DeleteMapping("/v2/warehouses/{warehouseId}/reviews/{reviewId}")
    public void delete(@PathVariable Integer warehouseId, @PathVariable Integer reviewId,
                       @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            WriteToClient.send(response, reviewsService.delete(reviewId, warehouseId, bearerToken), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseReviewInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseIdNotFoundException | WarehouseReviewNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        } catch(Exception exception) {
            exception.printStackTrace();
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
