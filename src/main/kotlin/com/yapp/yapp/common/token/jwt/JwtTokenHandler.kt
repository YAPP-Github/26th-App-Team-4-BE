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

    fun getTokenId(
        token: String,
        tokenType: TokenType,
    ): String {
        val claims = getValidClaims(token, tokenType)
        return claims.id
    }

    fun getUserId(
        token: String,
        tokenType: TokenType,
    ): Long {
        val claims = getValidClaims(token, tokenType)
        return claims.subject.toLong()
    }

    private fun getValidClaims(
        token: String,
        tokenType: TokenType,
    ): Claims {
        try {
            val validToken = parser.parseSignedClaims(token)
            val rawTokenType = validToken.header[JwtProperties.TOKEN_TYPE_CLAIM] as? String
            val actualTokenType = TokenType.from(rawTokenType)
            if (actualTokenType != tokenType) {
                throw CustomException(ErrorCode.TOKEN_TYPE_MISMATCH)
            }
            val claims = validToken.payload
            validateBlacklist(claims.id)
            return claims
        } catch (e: CustomException) {
            throw e
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }
    }

    private fun validateBlacklist(tokenId: String) {
        if (tokenBlacklist.contains(tokenId)) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        }
    }

    fun expire(tokenId: String) {
        tokenBlacklist.add(tokenId)
    }
}
