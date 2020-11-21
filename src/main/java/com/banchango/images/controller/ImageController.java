package com.banchango.images.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.images.service.S3UploaderService;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.exception.WarehouseAttachmentNotFoundException;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import com.banchango.warehouses.exception.WarehouseMainImageNotFoundException;
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

    // TODO : Test
    @DeleteMapping("/images/delete/{warehouseId}/{imageName}")
    public void deleteImage(@RequestHeader(name = "Authorization") String bearerToken, @PathVariable Integer warehouseId,
                             @PathVariable String imageName, HttpServletResponse response) {
        try {
            WriteToClient.send(response, s3UploaderService.deleteImage(bearerToken, imageName, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseAttachmentNotFoundException | WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            exception.printStackTrace();
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // TODO : TEST
    @DeleteMapping("/images/delete/main/{warehouseId}")
    public void deleteMainImage(@RequestHeader(name = "Authorization") String bearerToken,
                                @PathVariable Integer warehouseId, HttpServletResponse response) {
        try {
            WriteToClient.send(response, s3UploaderService.deleteMainImage(bearerToken, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseMainImageNotFoundException | WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            exception.printStackTrace();
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
