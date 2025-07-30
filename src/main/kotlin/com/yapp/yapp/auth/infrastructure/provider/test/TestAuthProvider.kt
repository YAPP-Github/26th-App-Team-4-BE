package com.yapp.yapp.auth.infrastructure.provider.test

import com.yapp.yapp.auth.domain.AuthProvider
import com.yapp.yapp.auth.domain.AuthUserInfo
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoApiClient
import org.springframework.stereotype.Component

@Component
class TestAuthProvider(
    private val kakaoApiClient: KakaoApiClient,
) : AuthProvider {
    override fun authenticate(
        token: String,
        nonce: String?,
    ): AuthUserInfo {
        kakaoApiClient.getOidcProperties()
        return TestAuthUserInfo()
    }

    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.TEST
    }
}
