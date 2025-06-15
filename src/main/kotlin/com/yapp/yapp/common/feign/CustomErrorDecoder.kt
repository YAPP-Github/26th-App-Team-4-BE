package com.yapp.yapp.common.feign

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import feign.Response
import feign.codec.ErrorDecoder
import io.github.oshai.kotlinlogging.KotlinLogging

class CustomErrorDecoder : ErrorDecoder {
    private val defaultDecoder = ErrorDecoder.Default()
    private val log = KotlinLogging.logger {}

    override fun decode(
        p0: String?,
        p1: Response?,
    ): Exception {
        val responseBody =
            p1?.body()
                ?.asInputStream()
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: "No response body"
        log.error {
            "Feign error - status: ${p1?.status()}, reason: ${p1?.reason()}, body: $responseBody"
        }
        return when (p1?.status()) {
            401 -> CustomException(ErrorCode.EXTERNAL_API_UNAUTHORIZED)
            403 -> CustomException(ErrorCode.EXTERNAL_API_FORBIDDEN)
            404 -> CustomException(ErrorCode.EXTERNAL_API_NOT_FOUND)
            in 400..499 -> CustomException(ErrorCode.EXTERNAL_API_CLIENT)
            in 500..599 -> CustomException(ErrorCode.EXTERNAL_API_SERVER)
            else -> defaultDecoder.decode(p0, p1)
        }
    }
}
