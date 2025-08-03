package com.yapp.yapp.auth.api.response

import com.yapp.yapp.common.token.jwt.TokenInfo
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.UserInfo

data class LoginResponse(
    val tokenResponse: TokenResponse,
    val user: UserResponse,
    val isNew: Boolean = false,
    val isRestore: Boolean = false,
) {
    constructor(tokenInfo: TokenInfo, userInfo: UserInfo, isNew: Boolean, isRestore: Boolean) : this(
        tokenResponse = TokenResponse(tokenInfo),
        user = UserResponse(userInfo),
        isNew = isNew,
        isRestore = isRestore,
    )
}

fun LoginResponse.accessToken() = this.tokenResponse.accessToken

fun LoginResponse.refreshToken() = this.tokenResponse.refreshToken
