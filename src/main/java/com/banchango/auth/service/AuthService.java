package com.banchango.auth.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.tools.ObjectMaker;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class AuthService {

    @SuppressWarnings("unchecked")
    public JSONObject refreshToken(String token) throws AuthenticateException {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        String userId = JwtTokenUtil.extractUserId(token);
        if(userId == null) throw new AuthenticateException();
        if(JwtTokenUtil.isTokenExpired(token)) throw new AuthenticateException();
        jsonObject.put("accessToken", JwtTokenUtil.generateAccessToken(Integer.parseInt(userId)));
        jsonObject.put("tokenType", "Bearer");
        return jsonObject;
    }
}
