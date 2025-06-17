package com.yapp.yapp.common.token.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenGenerator(
    private val jwtProperties: JwtProperties,
) {
    private val accessKey =
        Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProperties.accessSecret),
        )

    private val refreshKey =
        Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProperties.refreshSecret),
        )

    fun generateTokens(userId: Long): TokenInfo {
        val accessToken = generateAccessToken(userId)
        val refreshToken = generateRefreshToken(userId)
        return TokenInfo(accessToken, refreshToken)
    }

    private fun generateAccessToken(userId: Long): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .add(JwtProperties.TOKEN_TYPE_CLAIM, TokenType.ACCESS.name.lowercase())
            .and()
            .subject(userId.toString())
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .expiration(Date(now.time + jwtProperties.accessExpirationMillis))
            .signWith(accessKey)
            .compact()
    }

    private fun generateRefreshToken(userId: Long): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .add(JwtProperties.TOKEN_TYPE_CLAIM, TokenType.REFRESH.name.lowercase())
            .and()
            .subject(userId.toString())
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .expiration(Date(now.time + jwtProperties.refreshExpirationMillis))
            .signWith(refreshKey)
            .compact()
    }
}
