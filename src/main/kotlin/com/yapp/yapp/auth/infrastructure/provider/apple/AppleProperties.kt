package com.yapp.yapp.auth.infrastructure.provider.apple

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oidc.apple.api")
data class AppleProperties(
    val url: String,
    val clientId: List<String>,
)
