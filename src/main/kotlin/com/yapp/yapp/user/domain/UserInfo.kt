package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

data class UserInfo(
    val id: Long,
    val email: String,
    val nickname: String,
    val provider: ProviderType,
    val isNew: Boolean = false,
)
