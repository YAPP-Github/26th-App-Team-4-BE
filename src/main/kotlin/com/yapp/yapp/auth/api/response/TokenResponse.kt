package com.yapp.yapp.auth.api.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
