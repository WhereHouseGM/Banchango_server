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
    @GetMapping("/v3/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto getUserInfo(@PathVariable Integer userId, @RequestAttribute(name = "accessToken") String token) {
        return usersService.getUserInfo(userId, token);
    }

    @PostMapping("/v3/users/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public UserSigninResponseDto signIn(@Valid @RequestBody UserSigninRequestDto requestDto) throws Exception{
        // TODO : Test for INTERNAL_SERVER_ERROR
        if(true) {
            throw new Exception("AS");
        }
        return usersService.signIn(requestDto);
    }

    @PostMapping("/v3/users/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto signUp(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return usersService.signUp(requestDto);
    }

    @ValidateRequired
    @PatchMapping("/v3/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponseDto updateUserInfo(@Valid @RequestBody UserUpdateRequestDto requestDto, @PathVariable Integer userId,
                               @RequestAttribute(name = "accessToken") String token) {
        return usersService.updateUserInfo(requestDto, userId, token);
    }

    @PatchMapping("/v3/users/password-lost")
    @ResponseStatus(HttpStatus.OK)
    public BasicMessageResponseDto sendTemporaryPasswordToEmail(@Valid @RequestBody UserEmailSendRequestDto requestDto) {
        return usersService.sendTemporaryPasswordEmail(requestDto.getEmail());
    }

    @PatchMapping("/v3/users/change-password")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired
    public BasicMessageResponseDto changePassword(
        @Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        usersService.changePassword(accessToken, changePasswordRequestDto);
        return new BasicMessageResponseDto("비밀번호를 성공적으로 변경했습니다");
    }

    @PostMapping("/v3/users/{userId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired
    public BasicMessageResponseDto withdrawUser(
        @PathVariable Integer userId,
        @Valid @RequestBody UserWithdrawRequestDto userWithdrawRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        usersService.withdrawUser(accessToken, userId, userWithdrawRequestDto);
        return new BasicMessageResponseDto("탈퇴에 성공했습니다");
    }
}