package com.banchango.auth.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "secret_key";
    private static final int REFRESH_TOKEN_EXPIRATION = 86400000 * 7;
    private static final int ACCESS_TOKEN_EXPIRATION = 86400000;

    public static String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateAccessToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, userId.toString());
    }

    private static String createAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String generateRefreshToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, userId.toString());
    }

    private static String createRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token, Integer userId) {
        String userName = extractUserId(token);
        return (userName.equals(userId) && !isTokenExpired(token));
    }
}
