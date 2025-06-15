package com.yapp.yapp.common.cache.config

import com.yapp.yapp.common.cache.CacheNames
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
                        .entryTtl(Duration.ofDays(7))
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
                        .entryTtl(Duration.ofHours(6))
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
