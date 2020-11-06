package com.banchango.auth.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.tools.ObjectMaker;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthService {

    public org.json.simple.JSONObject refreshToken(String token) {
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        try {
            Jwts.parser().parse(token).
        }
    }
}
