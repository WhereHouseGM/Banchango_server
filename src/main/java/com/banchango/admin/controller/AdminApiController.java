package com.banchango.admin.controller;

import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseDto;
import com.banchango.admin.service.AdminService;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.users.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminApiController {

    private final AdminService adminService;

    @ValidateRequired(roles = UserRole.ADMIN)
    @GetMapping("/v3/admin/warehouses")
    @ResponseStatus(HttpStatus.OK)
    public List<WarehouseInsertRequestResponseDto> getWaitingWarehouses(@RequestAttribute(name = "accessToken") String token,
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

    // TODO : 창고 정보값 및 등록 신청 상태 수정하게 해주는 API
    @ValidateRequired(roles = UserRole.ADMIN)
    @PatchMapping("/v3/admin/warehouses/edit/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateWarehouses(@RequestAttribute(name = "accessToken") String token,
                                                            @PathVariable Integer warehouseId) {

    }

}
