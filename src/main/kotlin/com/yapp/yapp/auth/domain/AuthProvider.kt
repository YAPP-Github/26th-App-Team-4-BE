package com.yapp.yapp.auth.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

interface AuthProvider {
    fun authenticate(
        token: String,
        nonce: String?,
    ): AuthUserInfo

    fun supports(providerType: ProviderType): Boolean
}
