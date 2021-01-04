package com.banchango.admin.controller;

import com.banchango.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminApiController {

    private final AdminService adminService;


}
