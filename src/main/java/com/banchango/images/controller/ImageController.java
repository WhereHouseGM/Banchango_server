package com.banchango.images.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.images.service.S3UploaderService;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final S3UploaderService s3UploaderService;

    @PostMapping("/images/upload/{warehouseId}")
    @ResponseBody
    public void upload(@RequestPart(name = "file") MultipartFile multipartFile, HttpServletResponse response,
                       @RequestHeader(name = "Authorization") String bearerToken, @PathVariable Integer warehouseId) {
        try {
            WriteToClient.send(response, s3UploaderService.upload(multipartFile, bearerToken, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}