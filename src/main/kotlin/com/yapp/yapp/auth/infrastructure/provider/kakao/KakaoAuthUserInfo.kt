package com.yapp.yapp.auth.infrastructure.provider.kakao

import com.yapp.yapp.auth.domain.AuthUserInfo

class KakaoAuthUserInfo(
    private val email: String,
    private val name: String?,
    private val profile: String?,
) : AuthUserInfo {
    override fun getEmail(): String {
        return email
    }

    override fun getProfile(): String? {
        return profile
    }

    override fun getName(): String? {
        return name
    }
}
