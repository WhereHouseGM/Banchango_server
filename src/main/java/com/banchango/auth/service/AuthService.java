//package com.banchango.auth.service;
//
//import com.banchango.auth.exception.AuthenticateException;
//import com.banchango.auth.token.JwtTokenUtil;
//import com.banchango.tools.ObjectMaker;
//import lombok.NoArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//@NoArgsConstructor
//@Service
//public class AuthService {
//
//    public JSONObject refreshToken(String token) throws AuthenticateException {
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
//            throw new AuthenticateException();
//        }
//        String userIdOfToken = JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token));
//        jsonObject.put("accessToken", JwtTokenUtil.generateAccessToken(Integer.parseInt(userIdOfToken)));
//        jsonObject.put("tokenType", "Bearer");
//        return jsonObject;
//    }
//}
