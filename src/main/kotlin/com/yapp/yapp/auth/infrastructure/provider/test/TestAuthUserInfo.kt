package com.yapp.yapp.auth.infrastructure.provider.test

import com.yapp.yapp.auth.domain.AuthUserInfo
import java.util.UUID

class TestAuthUserInfo : AuthUserInfo {
    override fun getEmail(): String {
        return "test-${UUID.randomUUID()}@test.com"
    }
}
