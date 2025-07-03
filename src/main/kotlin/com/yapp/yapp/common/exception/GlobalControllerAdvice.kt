package com.yapp.yapp.common.exception

import com.yapp.yapp.common.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
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
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(CustomException::class)
    fun handleException(exception: CustomException): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = exception.errorCode
        return handleError(errorCode)
    }

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        NoResourceFoundException::class,
        NoHandlerFoundException::class,
        HttpMessageNotReadableException::class,
    )
    fun handleInvalidRequestException(request: HttpServletRequest): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = ErrorCode.INVALID_REQUEST
        return handleError(errorCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_
        logger.error { exception.message }
        return handleError(errorCode)
    }

    private fun handleError(errorCode: ErrorCode): ResponseEntity<ApiResponse<Unit>> {
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
