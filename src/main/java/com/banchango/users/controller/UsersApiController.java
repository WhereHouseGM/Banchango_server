package com.banchango.users.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailInUseException;
import com.banchango.users.exception.UserIdNotFoundException;
import com.banchango.users.exception.UserNotFoundException;
import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;

    // TODO : JWT Token Test
    @GetMapping("/v1/users/{userId}")
    public void getUserInfo(@PathVariable Integer userId, @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            if(userId == null) throw new Exception();
            WriteToClient.send(response, usersService.viewUserInfo(userId, bearerToken), HttpServletResponse.SC_OK);
        } catch(UserIdNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/v1/auth/sign-up")
    public void signUp(@RequestBody UserSignupRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, usersService.signUp(requestDto), HttpServletResponse.SC_CREATED);
        } catch(UserEmailInUseException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/v1/auth/sign-in")
    public void signIn(@RequestBody UserSigninRequestDto requestDto, HttpServletResponse response) {
        try {
            WriteToClient.send(response, usersService.signIn(requestDto), HttpServletResponse.SC_OK);
        } catch(UserNotFoundException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // TODO : JWT Token Test
    @PatchMapping("/v1/users/{userId}")
    public void updateUserInfo(@RequestBody UserSignupRequestDto requestDto, @PathVariable Integer userId, @RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            if(userId == null) throw new Exception();
            WriteToClient.send(response, usersService.updateUserInfo(userId, requestDto, bearerToken), HttpServletResponse.SC_OK);
        } catch(UserIdNotFoundException | UserEmailInUseException exception) {
            if(exception instanceof UserIdNotFoundException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_NOT_FOUND);
            }
            else if(exception instanceof UserEmailInUseException) {
                WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_CONFLICT);
            }
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
