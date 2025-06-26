package com.yapp.yapp.support

import com.deepromeet.atcha.support.fixture.UserFixture
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired

abstract class BaseSupportMethod {
    @Autowired
    lateinit var userRepository: UserRepository

    protected val objectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    protected fun getSavedUser(user: User = UserFixture.create()): User {
        return userRepository.save(user)
    }

    protected fun <T> convert(
        result: Any?,
        classType: Class<T>,
    ): T = objectMapper.convertValue(result, classType)
}
