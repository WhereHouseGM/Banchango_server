package com.banchango.admin.controller;

import com.banchango.admin.dto.EstimateStatusUpdateRequestDto;
import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseListDto;
import com.banchango.admin.service.AdminService;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.images.dto.ImageInfoResponseDto;
import com.banchango.images.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AdminApiController {

    private final AdminService adminService;
    private final S3UploaderService s3UploaderService;

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/warehouses")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseInsertRequestResponseListDto getWaitingWarehouses(@RequestAttribute(name = "accessToken") String token,
                                                                      @RequestParam(name = "page") Integer page,
                                                                      @RequestParam(name = "size") Integer size,
                                                                      @RequestParam(name = "status")WarehouseStatus status) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.findWaitingWarehouses(token, pageRequest, status);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/warehouses/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseAdminDetailResponseDto getSpecificWarehouseInfo(@RequestAttribute(name = "accessToken") String token,
                                                                    @PathVariable Integer warehouseId) {
        return adminService.getSpecificWarehouseInfo(token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @PutMapping("/v3/admin/warehouses/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseAdminDetailResponseDto updateWarehouses(@Valid @RequestBody WarehouseAdminUpdateRequestDto requestDto,
                                                    @RequestAttribute(name = "accessToken") String token,
                                                    @PathVariable Integer warehouseId) {
        return adminService.updateWarehouse(requestDto, token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @PostMapping("/v3/admin/images/{warehouseId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ImageInfoResponseDto uploadImage(@RequestPart(name = "file")MultipartFile file,
                                            @RequestAttribute(name = "accessToken") String token,
                                            @PathVariable Integer warehouseId) {
        return s3UploaderService.uploadExtraImageByAdmin(file, token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @PostMapping("/v3/admin/images/main/{warehouseId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ImageInfoResponseDto uploadMainImage(@RequestPart(name = "file") MultipartFile file,
                                                @RequestAttribute(name = "accessToken") String token,
                                                @PathVariable Integer warehouseId) {
        return s3UploaderService.uploadMainImageByAdmin(file, token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @DeleteMapping("/v3/admin/images/main/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public BasicMessageResponseDto deleteMainImage(@RequestAttribute(name = "accessToken") String token,
                                                   @PathVariable Integer warehouseId) {
        return s3UploaderService.deleteMainImageByAdmin(token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @DeleteMapping("/v3/admin/images/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public BasicMessageResponseDto deleteExtraImage(@RequestParam(name = "file") String fileName,
                                                    @RequestAttribute(name = "accessToken") String token,
                                                    @PathVariable Integer warehouseId) {
        return s3UploaderService.deleteExtraImageByAdmin(fileName, token, warehouseId);
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/estimates")
    @ResponseStatus(HttpStatus.OK)
    public EstimateSearchResponseDto getEstimates(
        @RequestParam(required = false)EstimateStatus status,
        @RequestParam Integer page,
        @RequestParam Integer size,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return new EstimateSearchResponseDto(adminService.getEstimates(accessToken, status, pageRequest));
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @PatchMapping("/v3/admin/estimates/{estimateId}/status")
    @ResponseStatus(HttpStatus.OK)
    public BasicMessageResponseDto updateEstimateStatus(
        @PathVariable Integer estimateId,
        @Valid @RequestBody EstimateStatusUpdateRequestDto estimateStatusUpdateRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        adminService.updateEstimateStatus(accessToken, estimateId, estimateStatusUpdateRequestDto);

        return new BasicMessageResponseDto("견적 문의 상태를 성공적으로 변경했습니다");
    }

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/estimates/{estimateId}/items")
    @ResponseStatus(HttpStatus.OK)
    public EstimateItemSearchResponseDto getEstimateItems(
        @PathVariable Integer estimateId,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return new EstimateItemSearchResponseDto(adminService.getEstimateItems(accessToken, estimateId));
    }
}
