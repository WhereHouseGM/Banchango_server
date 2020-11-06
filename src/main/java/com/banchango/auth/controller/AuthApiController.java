package com.banchango.auth.controller;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/v1/auth/refresh-token")
    public void refreshToken(@RequestHeader(name = "Authorization") String bearerToken) {
        try {
            if(bearerToken == null) throw new AuthenticateException();
            
        }
    }
}
