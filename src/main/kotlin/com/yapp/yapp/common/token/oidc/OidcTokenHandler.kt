package com.yapp.yapp.common.token.oidc

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.JwkSet
import io.jsonwebtoken.security.Jwks

class OidcTokenHandler(
    private val oidcProperties: OidcProperties,
) {
    fun parseEmail(token: String): String {
        try {
            val claims = validate(token)
            return claims["email"] as? String ?: throw JwtException("Missing 'email' in Claims")
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }
    }

    // https://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation
    private fun validate(token: String): Claims {
        val jwks = getJwkSet(oidcProperties.keySet)
        val parser =
            Jwts.parser()
                .requireIssuer(oidcProperties.issuer)
                .requireAudience(oidcProperties.clientId)
                .keyLocator { header ->
                    val kid = header["kid"] ?: throw JwtException("Missing 'kid' in JWT header")
                    val key =
                        jwks.find { it.id == kid }
                            ?: throw JwtException("Unknown 'kid' = $kid")
                    key.toKey()
                }.build()

        val claims = parser.parseSignedClaims(token).payload

        oidcProperties.nonce?.let { expectedNonce ->
            val actualNonce =
                claims["nonce"] as? String ?: throw JwtException("Missing 'nonce' in Claims")
            if (!expectedNonce.equals(actualNonce)) {
                throw CustomException(ErrorCode.TOKEN_INVALID)
            }
        }

        return claims
    }

    private fun getJwkSet(keys: String): JwkSet {
        return Jwks.setParser()
            .build()
            .parse(keys)
    }
}
