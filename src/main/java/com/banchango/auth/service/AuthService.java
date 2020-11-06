package com.banchango.auth.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.tools.ObjectMaker;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class AuthService {

    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject refreshToken(String token) throws AuthenticateException {
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        String userId = JwtTokenUtil.extractUserId(token);
        System.out.println(userId);
        if(userId == null) throw new AuthenticateException();
        if(JwtTokenUtil.isTokenExpired(token)) throw new AuthenticateException();
        jsonObject.put("accessToken", JwtTokenUtil.generateAccessToken(Integer.parseInt(userId)));
        jsonObject.put("tokenType", "Bearer");
        return jsonObject;
    }
}
