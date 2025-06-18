package com.yapp.yapp.common.token.jwt

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component

@Component
class JwtTokenHandler(
    private val jwtProperties: JwtProperties,
    private val tokenBlacklist: TokenBlacklist,
) {
    private val accessKey =
        Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProperties.accessSecret),
        )

    private val refreshKey =
        Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProperties.refreshSecret),
        )

    private val parser =
        Jwts.parser()
            .requireIssuer(jwtProperties.issuer)
            .keyLocator { header ->
                val tokenType =
                    header[JwtProperties.TOKEN_TYPE_CLAIM]
                        ?: throw JwtException("Missing 'token_type' in JWT header")

                when (tokenType) {
                    TokenType.ACCESS.name.lowercase() -> accessKey
                    TokenType.REFRESH.name.lowercase() -> refreshKey
                    else -> throw JwtException("Unknown token_type: $tokenType")
                }
            }.build()

    fun getUserId(token: String): Long {
        try {
            val claims = getValidClaims(token)
            return (claims.subject as String).toLong()
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }
    }

    private fun getValidClaims(token: String): Claims {
        validateBlacklist(token)
        return parser.parseSignedClaims(token).payload
    }

    private fun validateBlacklist(token: String) {
        if (tokenBlacklist.contains(token)) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        }
    }

    fun expire(token: String) {
        tokenBlacklist.add(token)
    }
}
