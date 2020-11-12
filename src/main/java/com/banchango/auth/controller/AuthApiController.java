package com.banchango.auth.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.service.AuthService;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.WriteToClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;

    // DONE
    @PostMapping("/v2/auth/refresh-token")
    public void refreshToken(@RequestHeader(name = "Authorization") String bearerToken, HttpServletResponse response) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            WriteToClient.send(response, authService.refreshToken(bearerToken), HttpServletResponse.SC_OK);
        } catch(AuthenticateException exception) {
            WriteToClient.send(response, ObjectMaker.getJSONObjectWithException(exception), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
