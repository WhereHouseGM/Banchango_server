package com.banchango.users.controller;

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

    @GetMapping("/v1/users/{userId}")
    public void getUserInfo(@PathVariable Integer userId, HttpServletResponse response) {
        try {
            if(userId == null) throw new Exception();
            org.json.simple.JSONObject jsonObject = usersService.viewUserInfo(userId);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_OK);
        } catch(UserIdNotFoundException exception) {
            org.json.simple.JSONObject jsonObject = ObjectMaker.getJSONObjectWithException(exception);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/v1/auth/sign-up")
    public void signUp(@RequestBody UserSignupRequestDto requestDto, HttpServletResponse response) {
        try {
            int id = usersService.signUp(requestDto);
            org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
            org.json.simple.JSONArray jsonArray = ObjectMaker.getSimpleJSONArray();
            org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
            jTemp.putAll(requestDto.convertMap(id));
            jsonArray.add(jTemp);
            jsonObject.put("User", jsonArray);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_CREATED);
        } catch(UserEmailInUseException exception) {
            org.json.simple.JSONObject jsonObject = ObjectMaker.getJSONObjectWithException(exception);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_CONFLICT);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/v1/auth/sign-in")
    public void signIn(@RequestBody UserSigninRequestDto requestDto, HttpServletResponse response) {
        try {
            org.json.simple.JSONObject jsonObject = usersService.signIn(requestDto);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_OK);
        } catch(UserNotFoundException exception) {
            org.json.simple.JSONObject jsonObject = ObjectMaker.getJSONObjectWithException(exception);
            WriteToClient.send(response, jsonObject, HttpServletResponse.SC_NOT_FOUND);
        } catch(Exception exception) {
            WriteToClient.send(response, null, HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
