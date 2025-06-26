package com.yapp.yapp.common.token.jwt.infrastructure.cache

import com.yapp.yapp.common.cache.CacheNames
import com.yapp.yapp.common.token.jwt.TokenBlacklist
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class SpringCacheBlacklist(
    private val cacheManager: CacheManager,
) : TokenBlacklist {
    private val cache = cacheManager.getCache(CacheNames.TOKEN_BLACKLIST)!!

    override fun add(tokenId: String) {
        cache.put(tokenId, true)
    }

    override fun contains(tokenId: String): Boolean {
        return (cache.get(tokenId)?.get() as? Boolean) == true
    }
}
