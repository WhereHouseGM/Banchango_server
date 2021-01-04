package com.banchango.admin.controller;

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

}
