package com.banchango.users.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.users.dto.*;
import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;

    @ValidateRequired
    @GetMapping("/v2/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto getUserInfo(@PathVariable Integer userId, @RequestAttribute(name = "accessToken") String token) {
        return usersService.getUserInfo(userId, token);
    }

    @PostMapping("/v2/users/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public UserSigninResponseDto signIn(@RequestBody UserSigninRequestDto requestDto) {
        return usersService.signIn(requestDto);
    }

    @PostMapping("/v2/users/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto signUp(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return usersService.signUp(requestDto);
    }

    @ValidateRequired
    @PatchMapping("/v2/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto updateUserInfo(@Valid @RequestBody UserSignupRequestDto requestDto, @PathVariable Integer userId,
                               @RequestAttribute(name = "accessToken") String token) {
        return usersService.updateUserInfo(requestDto, userId, token);
    }

    @PatchMapping("/v2/users/password-lost")
    @ResponseStatus(HttpStatus.OK)
    public BasicMessageResponseDto sendTemporaryPasswordToEmail(@Valid @RequestBody UserEmailSendRequestDto requestDto) {
        return usersService.sendTemporaryPasswordEmail(requestDto.getEmail());
    }
}