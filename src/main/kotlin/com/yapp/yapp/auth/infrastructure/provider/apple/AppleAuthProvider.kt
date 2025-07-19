package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.domain.AuthProvider
import com.yapp.yapp.auth.domain.AuthUserInfo
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.oidc.OidcTokenHandler
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
    private val appleApiClient: AppleApiClient,
) : AuthProvider {
    companion object {
        private const val EMAIL_CLAIM = "email"
    }

    override fun authenticate(
        token: String,
        nonce: String?,
    ): AuthUserInfo {
        val properties = appleApiClient.getOidcProperties()
        properties.nonce = nonce

        val handler = OidcTokenHandler(properties)
        val tokenClaims = handler.parseClaims(token)
        val email =
            tokenClaims[EMAIL_CLAIM] as String?
                ?: throw CustomException(ErrorCode.TOKEN_CLAIM_MISSING)

        return AppleAuthUserInfo(email)
    }

    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.APPLE
    }
}
