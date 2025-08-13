package com.yapp.yapp.support

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.yapp.support.fixture.RunningFixture
import com.yapp.yapp.support.fixture.TermFixture
import com.yapp.yapp.support.fixture.UserFixture
import com.yapp.yapp.support.fixture.UserGoalFixture
import org.springframework.beans.factory.annotation.Autowired

abstract class BaseSupportMethod {
    @Autowired
    lateinit var userFixture: UserFixture

    @Autowired
    lateinit var userGoalFixture: UserGoalFixture

    @Autowired
    lateinit var runningFixture: RunningFixture

    @Autowired
    lateinit var termFixture: TermFixture

    protected val objectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    protected fun <T> convert(
        result: Any?,
        classType: Class<T>,
    ): T = objectMapper.convertValue(result, classType)
}
