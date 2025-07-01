package com.yapp.yapp.common.exception

import com.yapp.yapp.common.logging.format.ErrorLogFormat
import com.yapp.yapp.common.web.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.MDC
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalControllerAdvice {
    companion object {
        const val REQUEST_ID = "requestId"
    }

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleInternalServerException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = ErrorCode.INTERNAL_SERVER
        return handleError(errorCode, exception)
    }

    @ExceptionHandler(CustomException::class)
    fun handleException(exception: CustomException): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = exception.errorCode
        return handleError(errorCode, exception)
    }

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        NoResourceFoundException::class,
        NoHandlerFoundException::class,
        HttpMessageNotReadableException::class,
    )
    fun handleInvalidRequestException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = ErrorCode.NOT_FOUND_REQUEST
        logger.warn { exception.message }
        return handleError(errorCode, exception)
    }

    private fun handleError(
        errorCode: ErrorCode,
        exception: Exception,
    ): ResponseEntity<ApiResponse<Unit>> {
        loggingError(errorCode, exception)
        return ResponseEntity.status(errorCode.status)
            .body(ApiResponse.error(errorCode.errorCode, errorCode.message))
    }

    private fun loggingError(
        errorCode: ErrorCode,
        exception: Exception,
    ) {
        val errorLog =
            ErrorLogFormat(
                logLevel = errorCode.logLevel,
                requestId = MDC.get(REQUEST_ID) ?: "N/A",
                errorCode = errorCode,
                message = exception.message ?: "N/A",
            )
        when (errorCode.logLevel) {
            LogLevel.WARN -> logger.warn { errorLog.toString() }
            LogLevel.ERROR -> logger.error { errorLog.toString() }
            else -> logger.info { errorLog.toString() }
        }
    }
}
