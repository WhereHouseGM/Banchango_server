package com.banchango.admin.controller;

import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseListDto;
import com.banchango.admin.service.AdminService;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.users.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AdminApiController {

    private final AdminService adminService;

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/warehouses")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseInsertRequestResponseListDto getWaitingWarehouses(@RequestAttribute(name = "accessToken") String token,
                                                                      @RequestParam(name = "page") Integer page,
                                                                      @RequestParam(name = "size") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.findWaitingWarehouses(token, pageRequest);
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

}
