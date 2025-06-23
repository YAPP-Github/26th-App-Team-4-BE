package com.yapp.yapp.auth.api.response

import com.yapp.yapp.user.api.response.UserResponse

data class LoginResponse(
    val tokenResponse: TokenResponse,
    val user: UserResponse,
    val isNew: Boolean = false,
)
