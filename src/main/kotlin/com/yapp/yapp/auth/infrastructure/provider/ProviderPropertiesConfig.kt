package com.yapp.yapp.auth.infrastructure.provider

import com.yapp.yapp.auth.infrastructure.provider.apple.AppleProperties
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [AppleProperties::class, KakaoProperties::class])
class ProviderPropertiesConfig
