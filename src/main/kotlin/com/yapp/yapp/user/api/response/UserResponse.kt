package com.yapp.yapp.user.api.response

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val profileImage: String,
    val provider: ProviderType,
)
