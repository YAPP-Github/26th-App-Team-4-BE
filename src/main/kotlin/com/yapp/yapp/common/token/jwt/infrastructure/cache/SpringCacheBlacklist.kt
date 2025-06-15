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

    override fun add(token: String) {
        cache.put(token, true)
    }

    override fun contains(token: String): Boolean {
        return (cache.get(token)?.get() as? Boolean) == true
    }
}
