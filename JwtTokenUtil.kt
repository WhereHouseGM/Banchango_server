package com.urssu.bum.incubating.security.util

import com.urssu.bum.incubating.security.SecurityProperty
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtTokenUtil @Autowired constructor(
        private val securityProperty: SecurityProperty
) {
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(securityProperty.SECRET).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return createAccessToken(claims, userDetails.username)
    }

    private fun createAccessToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + securityProperty.ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, securityProperty.SECRET).compact()
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return createRefreshToken(claims, userDetails.username)
    }

    private fun createRefreshToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + securityProperty.REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, securityProperty.SECRET).compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username.equals(userDetails.username) && !isTokenExpired(token))
    }
}
