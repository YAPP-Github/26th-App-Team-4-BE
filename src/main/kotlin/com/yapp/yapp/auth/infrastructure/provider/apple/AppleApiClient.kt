package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.infrastructure.provider.ApiClient
import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.token.oidc.OidcProperties
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class AppleApiClient(
    private val appleProperties: AppleProperties,
    private val appleFeignClient: AppleFeignClient,
) : ApiClient {
    companion object {
        private const val CACHE_KEY = "'applePublicKeyResponse'"
    }

    @Cacheable(cacheNames = [CacheNames.API_RESPONSE], key = CACHE_KEY)
    override fun getOidcProperties(): OidcProperties = fetchOidcProperties()

    @CachePut(cacheNames = [CacheNames.API_RESPONSE], key = CACHE_KEY)
    fun refreshOidcProperties(): OidcProperties = fetchOidcProperties()

    private fun fetchOidcProperties(): OidcProperties {
        val jwkSet = appleFeignClient.fetchJwks()
        return OidcProperties(
            keySet = jwkSet,
            issuer = appleProperties.url,
            clientId = appleProperties.clientId,
        )
    }
}
