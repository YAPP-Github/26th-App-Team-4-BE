package com.yapp.yapp.auth.infrastructure.provider.kakao

import com.yapp.yapp.auth.domain.AuthProvider
import com.yapp.yapp.auth.domain.AuthUserInfo
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.oidc.OidcProperties
import com.yapp.yapp.common.token.oidc.OidcTokenHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class KakaoAuthProvider(
    @Value("\${oidc.kakao.api.url}")
    private val issuer: String,
    @Value("\${oidc.kakao.api.clientId}")
    private val clientId: String,
    private val kakaoFeignClient: KakaoFeignClient,
) : AuthProvider {
    companion object {
        private const val EMAIL_CLAIM = "email"
    }

    override fun authenticate(
        token: String,
        nonce: String?,
    ): AuthUserInfo {
        val properties = getProperties()
        properties.nonce = nonce

        val handler = OidcTokenHandler(properties)
        val claims = handler.parseClaims(token)
        val email =
            claims[EMAIL_CLAIM] as String? ?: throw CustomException(ErrorCode.TOKEN_CLAIM_MISSING)

        return KakaoAuthUserInfo(email)
    }

    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.KAKAO
    }

    @Cacheable(cacheNames = [CacheNames.API_RESPONSE], key = "'kakaoPublicKeyResponse'")
    fun getProperties(): OidcProperties {
        val jwkSet = kakaoFeignClient.fetchJwks()
        return OidcProperties(keySet = jwkSet, issuer = issuer, clientId = clientId)
    }
}
