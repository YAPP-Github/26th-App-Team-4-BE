package com.yapp.yapp.user.api.response

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

data class UserResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val provider: ProviderType,
)
