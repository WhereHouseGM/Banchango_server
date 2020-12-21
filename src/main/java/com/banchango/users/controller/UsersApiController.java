package com.banchango.users.controller;

import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.users.dto.*;
import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // DONE
//    @PostMapping("/v2/users/sign-up")
//    public void signUp(@RequestBody UserSignupRequestDto requestDto, HttpServletResponse response) {
//        try {
//            WriteToClient.send(response, usersService.signUp(requestDto), HttpServletResponse.SC_CREATED);
//        } catch(UserEmailInUseException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//
//    // DONE
//    @PostMapping("/v2/users/sign-in")
//    public void signIn(@RequestBody UserSigninRequestDto requestDto, HttpServletResponse response) {
//        try {
//            WriteToClient.send(response, usersService.signIn(requestDto), HttpServletResponse.SC_OK);
//        } catch(UserNotFoundException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//
//    // DONE
//    @PatchMapping("/v2/users/{userId}")
//    public void updateUserInfo(@RequestBody UserSignupRequestDto requestDto, @PathVariable Integer userId, @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
//        try {
//            if(bearerToken == null) throw new AuthenticateException();
//            if(userId == null) throw new Exception();
//            WriteToClient.send(response, usersService.updateUserInfo(userId, requestDto, bearerToken), HttpServletResponse.SC_OK);
//        } catch(UserException exception) {
//            if(exception instanceof UserIdNotFoundException) {
//                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
//            } else if(exception instanceof UserEmailInUseException) {
//                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
//            } else if(exception instanceof UserNotFoundException) {
//                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
//            } else if(exception instanceof UserInvalidAccessException) {
//                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
//            }
//        } catch(AuthenticateException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/v2/users/password-lost")
//    public void sendTemporaryPasswordToEmail(@RequestBody UserEmailSendRequestDto requestDto, HttpServletResponse response) {
//        try {
//            WriteToClient.send(response, usersService.sendTemporaryPasswordEmail(requestDto.getEmail()), HttpServletResponse.SC_OK);
//        } catch(UserEmailNotFoundException exception){
//            exception.printStackTrace();
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
//        }catch (Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
}