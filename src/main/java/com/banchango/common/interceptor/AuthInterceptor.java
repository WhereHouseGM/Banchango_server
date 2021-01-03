package com.banchango.common.interceptor;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.exception.ForbiddenException;
import com.banchango.domain.users.UserRole;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_SCHEME = "Bearer";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isHandlerMethod(handler)) {
            HandlerMethod method = (HandlerMethod) handler;
            boolean loginRequired = method.getMethod().isAnnotationPresent(ValidateRequired.class);

            if (loginRequired) {
                String authorizationHeader = extractAuthorizationHeader(request);
                String accessToken = extractAccessToken(authorizationHeader);
                if(JwtTokenUtil.isTokenExpired(accessToken)) throw new AuthenticateException("JWT Token is expired.");

                if(shouldAuthorize(method)) authorizeRequest(method, accessToken);
                request.setAttribute(ACCESS_TOKEN, accessToken);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isHandlerMethod(Object handler) {
        return handler instanceof HandlerMethod;
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(authorizationHeader == null) throw new AuthenticateException("Authorization Header is missing.");
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
        if(!scheme.equals(BEARER_SCHEME)) throw new AuthenticateException("Scheme should be Bearer.");
    }

    private boolean shouldAuthorize(HandlerMethod method) {
        ValidateRequired annotation = method.getMethodAnnotation(ValidateRequired.class);
        return annotation.roles().length > 0;
    }

    private void authorizeRequest(HandlerMethod method, String accessToken) {
        ValidateRequired annotation = method.getMethodAnnotation(ValidateRequired.class);
        UserRole accessTokenRole = JwtTokenUtil.extractUserRole(accessToken);
        boolean isAuthorized = Arrays.stream(annotation.roles()).anyMatch(role -> role == accessTokenRole);

        if(!isAuthorized) throw new ForbiddenException("해당 요청을 수행할 수 있는 권한이 없습니다");
    }
}
