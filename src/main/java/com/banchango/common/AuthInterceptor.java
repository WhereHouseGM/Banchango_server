package com.banchango.common;

import com.banchango.auth.exception.AuthenticateException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_SCHEME = "Bearer";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isHandlerMethod(handler)) {
            HandlerMethod method = (HandlerMethod) handler;
            boolean loginRequired = method.getMethod().isAnnotationPresent(LoginRequired.class);

            if(loginRequired) {
                String authorizationHeader = extractAuthorizationHeader(request);
                String accessToken = extractAccessToken(authorizationHeader);

                request.setAttribute(ACCESS_TOKEN, accessToken);
            }
        }

        return super.preHandle(request, response, handler);
    }

    private boolean isHandlerMethod(Object handler) {
        return handler instanceof HandlerMethod;
    }
    
    private String extractAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(authorizationHeader == null) throw new AuthenticateException("Authorization Header is Missing");
        return authorizationHeader;
    }

    private String extractAccessToken(String authorizationHeader) {
        String[] splits = authorizationHeader.split(" ");
        validateAuthorizationHeader(splits);

        String accessToken = splits[1];
        return accessToken;
    }
    private void validateAuthorizationHeader(String[] authorizationHeaderSplits) {
        if(authorizationHeaderSplits.length != 2) throw new AuthenticateException("Malformed Authorization Header");
        String scheme = authorizationHeaderSplits[0];
        if(!scheme.equals(BEARER_SCHEME)) throw new AuthenticateException("Scheme should be Bearer");
    }
}
