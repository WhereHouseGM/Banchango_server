package com.banchango.users.controller;

import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;

    @GetMapping("/v1/users/{userId}")
    public void getUserInfo(@PathVariable Integer userId, HttpServletResponse response) {

    }
}
