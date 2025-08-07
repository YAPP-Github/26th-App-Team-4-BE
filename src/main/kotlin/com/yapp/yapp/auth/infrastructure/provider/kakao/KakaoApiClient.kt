package com.yapp.yapp.auth.infrastructure.provider.kakao

import com.yapp.yapp.auth.infrastructure.provider.ApiClient
import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.token.oidc.OidcProperties
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class KakaoApiClient(
    private val kakaoProperties: KakaoProperties,
    private val kakaoFeignClient: KakaoFeignClient,
) : ApiClient {
    companion object {
        private const val CACHE_KEY = "'kakaoPublicKeyResponse'"
    }

    @Cacheable(cacheNames = [CacheNames.API_RESPONSE], key = CACHE_KEY)
    override fun getOidcProperties(): OidcProperties = fetchOidcProperties()

    @CachePut(cacheNames = [CacheNames.API_RESPONSE], key = CACHE_KEY)
    fun refreshOidcProperties(): OidcProperties = fetchOidcProperties()

    private fun fetchOidcProperties(): OidcProperties {
        val jwkSet = kakaoFeignClient.fetchJwks()
        return OidcProperties(
            keySet = jwkSet,
            issuer = kakaoProperties.url,
            clientId = kakaoProperties.clientId,
        )
    }
}
