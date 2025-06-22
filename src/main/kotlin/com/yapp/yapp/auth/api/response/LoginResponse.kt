package com.yapp.yapp.auth.api.response

data class LoginResponse(
    val tokenResponse: TokenResponse,
    val isNew: Boolean = false,
)
