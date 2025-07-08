package com.yapp.yapp.auth.infrastructure.provider.kakao

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oidc.kakao.api")
data class KakaoProperties(
    val url: String,
    val clientId: List<String>,
)
