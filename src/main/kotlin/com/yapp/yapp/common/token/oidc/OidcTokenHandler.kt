package com.yapp.yapp.common.token.oidc

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Jwks
import java.util.logging.Logger

class OidcTokenHandler(
    private val oidcProperties: OidcProperties,
) {
    private val logger = Logger.getLogger(this::class.java.simpleName)
    val jwks =
        Jwks.setParser()
            .build()
            .parse(oidcProperties.keySet)

    val parser =
        Jwts.parser()
            .requireIssuer(oidcProperties.issuer)
            .keyLocator { header ->
                val kid = header["kid"] ?: throw JwtException("Missing 'kid' in JWT header")
                val key =
                    jwks.find { it.id == kid }
                        ?: throw JwtException("Unknown 'kid' = $kid")
                key.toKey()
            }.build()

    fun parseClaims(token: String): Map<String, Any?> {
        try {
            val claims = getValidClaims(token)
            return claims.entries.associate { it.key to it.value }
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            logger.warning { e.message }
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }
    }

    // https://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation
    private fun getValidClaims(token: String): Claims {
        val claims = parser.parseSignedClaims(token).payload

        val audClaim = claims["aud"]
        val allowedAudiences = oidcProperties.clientId

        val isValidAudience =
            when (audClaim) {
                is String -> allowedAudiences.contains(audClaim)
                is Collection<*> -> audClaim.any { it is String && allowedAudiences.contains(it) }
                else -> false
            }

        if (!isValidAudience) {
            throw JwtException("Invalid audience: $audClaim")
        }

        oidcProperties.nonce?.let { expectedNonce ->
            val actualNonce =
                claims["nonce"] as? String ?: throw JwtException("Missing 'nonce' in Claims")
            if (!expectedNonce.equals(actualNonce)) {
                throw CustomException(ErrorCode.TOKEN_INVALID)
            }
        }

        return claims
    }
}
