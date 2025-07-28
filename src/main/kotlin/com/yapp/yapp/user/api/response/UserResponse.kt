package com.yapp.yapp.user.api.response

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserInfo

data class UserResponse(
    val userId: Long,
    val nickname: String,
    val email: String,
    val provider: ProviderType,
    val runnerType: RunnerType?,
) {
    constructor(user: User) : this(
        userId = user.id,
        nickname = user.nickname,
        email = user.email,
        provider = user.provider,
        runnerType = user.runnerType,
    )

    constructor(userInfo: UserInfo) : this(
        userId = userInfo.id,
        nickname = userInfo.nickname,
        email = userInfo.email,
        provider = userInfo.provider,
        runnerType = userInfo.runnerType,
    )
}
