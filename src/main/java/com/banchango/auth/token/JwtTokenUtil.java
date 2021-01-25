package com.banchango.auth.token;


import com.banchango.auth.exception.AuthenticateException;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "secret_key";
    private static final int REFRESH_TOKEN_EXPIRATION = 86400000 * 7;
    private static final int ACCESS_TOKEN_EXPIRATION = 86400000;

    public static UserRole extractUserRole(String token) {
        return extractClaim(token, JwtTokenUtil::getUserRole);
    }

    public static UserType extractUserType(String token) {
        return extractClaim(token, JwtTokenUtil::getUserType);
    }

    private static UserRole getUserRole(Claims claim) {
        String roleString = claim.get("role", String.class);
        if(roleString == null) throw new AuthenticateException("Jwt Claim에 role이 없습니다.");
        return UserRole.valueOf(roleString);
    }

    private static UserType getUserType(Claims claim) {
        String typeString = claim.get("type", String.class);
        if(typeString == null) throw new AuthenticateException("Jwt Claim에 type이 없습니다.");
        return UserType.valueOf(typeString);
    }

    public static int extractUserId(String token) {
        return extractClaim(token, JwtTokenUtil::getUserId);
    }

    private static int getUserId(Claims claim) {
        Integer userId = claim.get("userId", Integer.class);
        if(userId == null) throw new AuthenticateException("Jwt Claim에 userId가 없습니다.");
        return userId;
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token){
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch(ExpiredJwtException expiredJwtException) {
            throw new AuthenticateException("Jwt 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new AuthenticateException("지원되지 않는 Jwt 토큰입니다.");
        } catch (MalformedJwtException malformedJwtException) {
            throw new AuthenticateException("잘못된 형식의 Jwt 토큰입니다.");
        } catch (SignatureException signatureException) {
            throw new AuthenticateException("Jwt Signature이 잘못된 값입니다.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new AuthenticateException("Jwt 헤더 값이 잘못되었습니다.");
        }
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateAccessToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole());
        claims.put("type", user.getType());
        return createToken(claims, ACCESS_TOKEN_EXPIRATION);
    }

    public static String generateAccessToken(Integer userId, UserRole role, UserType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("type", type);
        return createToken(claims, ACCESS_TOKEN_EXPIRATION);
    }

    public static String generateRefreshToken(Integer userId, UserRole role, UserType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("type", type);
        return createToken(claims, REFRESH_TOKEN_EXPIRATION);
    }

    private static String createToken(Map<String, Object> claims, int expireationTimeInMs) {
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireationTimeInMs))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
