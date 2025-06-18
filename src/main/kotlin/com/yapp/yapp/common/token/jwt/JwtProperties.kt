package com.yapp.yapp.common.token.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    companion object {
        val TOKEN_TYPE_CLAIM = "token_type"
    }

    lateinit var accessSecret: String
    var accessExpirationMillis: Long = 0
    lateinit var refreshSecret: String
    var refreshExpirationMillis: Long = 0
    lateinit var issuer: String
}
