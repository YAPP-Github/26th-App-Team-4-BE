package com.yapp.yapp.auth.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class AuthManager(
    val authProviders: List<AuthProvider>,
) {
    fun authenticate(
        providerType: ProviderType,
        token: String,
        nonce: String?,
    ): AuthUserInfo {
        val provider = getProvider(providerType)
        return provider.authenticate(token, nonce)
    }

    private fun getProvider(providerType: ProviderType): AuthProvider {
        return authProviders.firstOrNull { it.supports(providerType) }
            ?: throw CustomException(ErrorCode.UNSUPPORTED_PROVIDER_TYPE)
    }
}
