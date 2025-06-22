package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.domain.AuthUserInfo

class AppleAuthUserInfo(
    private val email: String,
) : AuthUserInfo {
    override fun getEmail(): String {
        return email
    }

    override fun getProfile(): String? {
        return null
    }

    override fun getName(): String? {
        return null
    }
}
