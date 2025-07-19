package com.yapp.yapp.common.cache.config

import com.yapp.yapp.common.cache.CacheNames
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class RedisCacheConfig {
    @Value("\${jwt.refresh-expiration-millis}")
    private val tokenBlacklistMillisecondClockSource: Long = 604800000

    @Value("\${oidc.response-millis}")
    private val apiResponseMillisecondClockSource: Long = 3600000

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(60))
            .disableCachingNullValues()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    StringRedisSerializer(),
                ),
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(),
                ),
            )

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer =
        RedisCacheManagerBuilderCustomizer { builder ->
            builder
                .withCacheConfiguration(
                    CacheNames.TOKEN_BLACKLIST,
                    RedisCacheConfiguration.defaultCacheConfig()
                        .computePrefixWith { cacheName -> "$cacheName::" }
                        .entryTtl(Duration.ofMillis(tokenBlacklistMillisecondClockSource))
                        .disableCachingNullValues()
                        .serializeKeysWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                StringRedisSerializer(),
                            ),
                        )
                        .serializeValuesWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                GenericJackson2JsonRedisSerializer(),
                            ),
                        ),
                )
                .withCacheConfiguration(
                    CacheNames.API_RESPONSE,
                    RedisCacheConfiguration.defaultCacheConfig()
                        .computePrefixWith { cacheName -> "$cacheName::" }
                        .entryTtl(Duration.ofMillis(apiResponseMillisecondClockSource))
                        .disableCachingNullValues()
                        .serializeKeysWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                StringRedisSerializer(),
                            ),
                        )
                        .serializeValuesWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                GenericJackson2JsonRedisSerializer(),
                            ),
                        ),
                )
        }
}
