package com.yapp.yapp.auth.api.response

import com.yapp.yapp.common.token.jwt.TokenInfo

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    constructor(tokenInfo: TokenInfo) : this(
        accessToken = tokenInfo.accessToken,
        refreshToken = tokenInfo.refreshToken,
    )
}
