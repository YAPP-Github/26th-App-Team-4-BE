package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType

data class UserInfo(
    val id: Long,
    val email: String,
    val nickname: String,
    val provider: ProviderType,
    val isNew: Boolean = false,
) {
    constructor(user: User, isNew: Boolean = false) : this(
        id = user.id,
        email = user.email,
        nickname = user.nickname,
        provider = user.provider,
        isNew = isNew,
    )
}
