package com.banchango.users.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.users.dto.UserEmailSendRequestDto;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.*;
import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;

    // DONE
    @ValidateRequired
    @GetMapping("/v2/users/{userId}")
    public void getUserInfo(@PathVariable Integer userId, @RequestAttribute(name = "accessToken") String token) {

//        try {
//            if(bearerToken == null) throw new AuthenticateException();
//            if(userId == null) throw new Exception();
//            WriteToClient.send(response, usersService.viewUserInfo(userId, bearerToken), HttpServletResponse.SC_OK);
//        } catch(UserIdNotFoundException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
//        } catch(AuthenticateException exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
//        } catch(Exception exception) {
//            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
//        }
    }

    // DONE
    @PostMapping("/v2/users/sign-up")
    public void signUp(@RequestBody UserSignupRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, usersService.signUp(requestDto), HttpServletResponse.SC_CREATED);
        } catch(UserEmailInUseException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @PostMapping("/v2/users/sign-in")
    public void signIn(@RequestBody UserSigninRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, usersService.signIn(requestDto), HttpServletResponse.SC_OK);
        } catch(UserNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // DONE
    @PatchMapping("/v2/users/{userId}")
    public void updateUserInfo(@RequestBody UserSignupRequestDto requestDto, @PathVariable Integer userId, @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            if(userId == null) throw new Exception();
            WriteToClient.send(response, usersService.updateUserInfo(userId, requestDto, bearerToken), HttpServletResponse.SC_OK);
        } catch(UserException exception) {
            if(exception instanceof UserIdNotFoundException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
            } else if(exception instanceof UserEmailInUseException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
            } else if(exception instanceof UserNotFoundException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
            } else if(exception instanceof UserInvalidAccessException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_FORBIDDEN);
            }
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/v2/users/password-lost")
    public void sendTemporaryPasswordToEmail(@RequestBody UserEmailSendRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, usersService.sendTemporaryPasswordEmail(requestDto.getEmail()), HttpServletResponse.SC_OK);
        } catch(UserEmailNotFoundException exception){
            exception.printStackTrace();
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NO_CONTENT);
        }catch (Exception exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}