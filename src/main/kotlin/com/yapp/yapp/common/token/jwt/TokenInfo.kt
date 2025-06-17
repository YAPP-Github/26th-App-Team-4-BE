package com.yapp.yapp.common.token.jwt

data class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
)
