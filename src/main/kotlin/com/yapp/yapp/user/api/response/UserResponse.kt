package com.yapp.yapp.user.api.response

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

data class UserResponse(
    val userId: Long,
    val nickname: String,
    val email: String,
    val provider: ProviderType,
)
