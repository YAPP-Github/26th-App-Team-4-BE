package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.domain.AuthProvider
import com.yapp.yapp.auth.domain.AuthUserInfo
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoAuthUserInfo
import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.token.oidc.OidcProperties
import com.yapp.yapp.common.token.oidc.OidcTokenHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
    @Value("\${oidc.apple.api.url}")
    private val issuer: String,
    @Value("\${oidc.apple.api.clientId}")
    private val clientId: String,
    private val appleFeignClient: AppleFeignClient,
) : AuthProvider {
    override fun authenticate(
        token: String,
        nonce: String?,
    ): AuthUserInfo {
        val properties = getProperties()
        properties.nonce = nonce

        val handler = OidcTokenHandler(properties)
        val email = handler.parseEmail(token)

        return KakaoAuthUserInfo(email)
    }

    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.APPLE
    }

    @Cacheable(cacheNames = [CacheNames.API_RESPONSE], key = "'applePublicKeyResponse'")
    fun getProperties(): OidcProperties {
        val jwkSet = appleFeignClient.fetchJwks()
        return OidcProperties(keySet = jwkSet, issuer = issuer, clientId = clientId)
    }
}
