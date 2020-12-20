package com.banchango.users.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.common.LoginRequired;
import com.banchango.domain.users.Users;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import com.banchango.users.dto.UserEmailSendRequestDto;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.*;
import com.banchango.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    private final UsersService usersService;

    // DONE
    @GetMapping("/v2/users/{userId}")
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
            WriteToClient.send(response, ObjectMaker.getJSONObjectOfBadRequest(), HttpServletResponse.SC_BAD_REQUEST);
        }
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
    @LoginRequired
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/v2/users/{userId}")
    public Users updateUserInfo(
            @PathVariable Integer userId,
            @RequestBody UserSignupRequestDto requestDto,
            @RequestAttribute("accessToken") String accessToken
    ) {
        Users updatedUser = usersService.updateUserInfo(userId, requestDto, accessToken);
        return updatedUser;
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