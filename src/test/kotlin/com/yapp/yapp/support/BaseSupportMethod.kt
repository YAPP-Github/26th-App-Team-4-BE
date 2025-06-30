package com.yapp.yapp.support

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class BaseSupportMethod {
    protected val objectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    protected fun <T> convert(
        result: Any?,
        classType: Class<T>,
    ): T = objectMapper.convertValue(result, classType)
}
