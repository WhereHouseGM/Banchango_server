package com.banchango.images.controller;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.banchango.auth.exception.AuthenticateException;
import com.banchango.images.service.S3UploaderService;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.warehouses.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final S3UploaderService s3UploaderService;

    // DONE
    @PostMapping("/v2/images/upload/{warehouseId}")
    @ResponseBody
    public void upload(@RequestPart(name = "file") MultipartFile multipartFile, HttpServletResponse response,
                       @RequestHeader(name = "Authorization") String bearerToken, @PathVariable Integer warehouseId) {
        try {
            WriteToClient.send(response, s3UploaderService.upload(multipartFile, bearerToken, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseAttachmentLimitException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_ACCEPTABLE);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @PostMapping("/v2/images/upload/main/{warehouseId}")
    @ResponseBody
    public void uploadMain(@RequestPart(name = "file") MultipartFile multipartFile, HttpServletResponse response,
                           @RequestHeader(name = "Authorization") String bearerToken, @PathVariable Integer warehouseId) {
        try {
            WriteToClient.send(response, s3UploaderService.uploadMainImage(multipartFile, bearerToken, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseMainImageLimitException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_ACCEPTABLE);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @DeleteMapping("/v2/images/delete")
    public void deleteImage(@RequestHeader(name = "Authorization") String bearerToken, @RequestParam(name = "warehouseId") Integer warehouseId,
                             @RequestParam(name = "file") String imageName, HttpServletResponse response) {
        try {
            WriteToClient.send(response, s3UploaderService.deleteImage(bearerToken, imageName, warehouseId), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(WarehouseInvalidAccessException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
        } catch(WarehouseAttachmentNotFoundException | WarehouseIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @DeleteMapping("/v2/images/delete/main/{warehouseId}")
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
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
