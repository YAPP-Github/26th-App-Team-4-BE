package com.deepromeet.atcha.support.fixture

import com.yapp.yapp.user.domain.User

object UserFixture {
    fun create(
        name: String = "test name",
        email: String = "test email",
        profile: String = "test profile",
    ): User =
        User(
            name = name,
            email = email,
            profile = profile,
        )
}
