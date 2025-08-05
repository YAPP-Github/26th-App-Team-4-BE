package com.yapp.yapp.auth.infrastructure.cache

import com.yapp.yapp.auth.infrastructure.provider.apple.AppleApiClient
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoApiClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CacheUpdateScheduler(
    private val kakaoApiClient: KakaoApiClient,
    private val appleApiClient: AppleApiClient,
) {
    private val logger = KotlinLogging.logger {}

    @Scheduled(
        initialDelayString = "\${oidc.initial-refresh-delay}",
        fixedDelayString = "\${oidc.response-refresh-millis}",
    )
    fun refreshOidcProperties() {
        kakaoApiClient.refreshOidcProperties()
        appleApiClient.refreshOidcProperties()
        logger.info { "[${java.time.Instant.now()}] Oidc Response 캐시 갱신 완료" }
    }
}
