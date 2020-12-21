package com.banchango.auth.token;


import com.banchango.auth.exception.AuthenticateException;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "secret_key";
    private static final int REFRESH_TOKEN_EXPIRATION = 86400000 * 7;
    private static final int ACCESS_TOKEN_EXPIRATION = 86400000;

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

    public static String generateAccessToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, ACCESS_TOKEN_EXPIRATION);
    }

    public static String generateRefreshToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
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
