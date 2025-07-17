package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.infrastructure.provider.ApiClient
import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.token.oidc.OidcProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class AppleApiClient(
    private val appleProperties: AppleProperties,
    private val appleFeignClient: AppleFeignClient,
) : ApiClient {
    @Cacheable(cacheNames = [CacheNames.API_RESPONSE], key = "'applePublicKeyResponse'")
    override fun getOidcProperties(): OidcProperties {
        val jwkSet = appleFeignClient.fetchJwks()
        return OidcProperties(
            keySet = jwkSet,
            issuer = appleProperties.url,
            clientId = appleProperties.clientId,
        )
    }
}
