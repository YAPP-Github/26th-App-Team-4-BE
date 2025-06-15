package com.yapp.yapp.common.token.jwt

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
