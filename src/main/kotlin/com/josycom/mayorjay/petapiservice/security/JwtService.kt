package com.josycom.mayorjay.petapiservice.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Base64
import java.util.Date
import java.util.function.Function
@Component
class JwtService {

    fun generateToken(username: String): String = generateToken(HashMap(), username)

    private fun generateToken(extraClaims: Map<String, Any>, username: String): String =
        Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + EXPIRY_TIME))
            .signWith(signingKey(), SignatureAlgorithm.HS256)
            .compact()

    private fun <T> extractSingleClaim(token: String, claimsResolver: Function<Claims, T>): T? {
        val claims = extractAllClaims(token)
        return claims?.let { claimsResolver.apply(it) }
    }

    private fun extractAllClaims(token: String): Claims? = try {
        Jwts.parserBuilder()
            .setSigningKey(signingKey())
            .build()
            .parseClaimsJws(token)
            .body
    } catch (ex: Exception) {
        throw IllegalArgumentException("Invalid JWT Token")
    }

    private fun signingKey(): Key {
        val keyBytes = Base64.getDecoder().decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenValid(token: String, username: String): Boolean {
        return extractUsername(token).equals(username) && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String? = extractSingleClaim(token) { claims -> claims.subject }

    private fun isTokenExpired(token: String): Boolean {
        val expiry = extractSingleClaim(token) { claims -> claims.expiration } ?: Date()
        return expiry.before(Date())
    }

    companion object {
        private const val SECRET_KEY = "QXdhcmVEaXNwbGFjZVN0ZWFtQ29uZmxpY3RPbmlvblJlc3RyYWluVGlnZXJBcmd1bWVudEVsYm93QXVkaWVuY2U="
        private const val EXPIRY_TIME = 1000 * 60 * 15
    }
}

