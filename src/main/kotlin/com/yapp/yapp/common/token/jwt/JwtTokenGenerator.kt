package com.yapp.yapp.common.token.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

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
        val id = UUID.randomUUID().toString()
        val accessToken = generateAccessToken(userId, id)
        val refreshToken = generateRefreshToken(userId, id)
        return TokenInfo(accessToken, refreshToken)
    }

    private fun generateAccessToken(
        userId: Long,
        id: String,
    ): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .add(JwtProperties.TOKEN_TYPE_CLAIM, TokenType.ACCESS.name.lowercase())
            .and()
            .subject(userId.toString())
            .id(id)
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .expiration(Date(now.time + jwtProperties.accessExpirationMillis))
            .signWith(accessKey)
            .compact()
    }

    private fun generateRefreshToken(
        userId: Long,
        id: String,
    ): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .add(JwtProperties.TOKEN_TYPE_CLAIM, TokenType.REFRESH.name.lowercase())
            .and()
            .subject(userId.toString())
            .id(id)
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .expiration(Date(now.time + jwtProperties.refreshExpirationMillis))
            .signWith(refreshKey)
            .compact()
    }
}
