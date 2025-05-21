package com.yapp.yapp.common.exception

import com.yapp.yapp.common.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(CustomException::class)
    fun handleException(exception: CustomException): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = exception.errorCode
        logging(errorCode)
        return ResponseEntity.status(errorCode.status)
            .body(ApiResponse.error(errorCode.errorCode, errorCode.message))
    }

    fun logging(errorCode: ErrorCode) {
        when (errorCode.logLevel) {
            LogLevel.WARN -> logger.warn { errorCode.message }
            LogLevel.ERROR -> logger.error { errorCode.message }
            else -> logger.info { errorCode.message }
        }
    }
}
