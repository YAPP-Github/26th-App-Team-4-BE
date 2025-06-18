package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.auth.domain.AuthUserInfo
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

class AppleAuthUserInfo(
    private val email: String,
) : AuthUserInfo {
    override fun getEmail(): String {
        return email
    }

    override fun getUserId(): Long {
        throw CustomException(ErrorCode.UNSUPPORTED_ATTRIBUTE)
    }
}
