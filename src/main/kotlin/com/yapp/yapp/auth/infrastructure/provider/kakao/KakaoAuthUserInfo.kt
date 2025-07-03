package com.yapp.yapp.auth.infrastructure.provider.kakao

import com.yapp.yapp.auth.domain.AuthUserInfo

class KakaoAuthUserInfo(
    private val email: String,
) : AuthUserInfo {
    override fun getEmail(): String {
        return email
    }
}
