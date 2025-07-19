package com.yapp.yapp.common.logging.format

import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.boot.logging.LogLevel

data class ErrorLogFormat(
    val type: String = "ERROR",
    val requestId: String,
    val logLevel: LogLevel,
    val errorCode: ErrorCode,
    val message: String,
) {
    override fun toString(): String {
        return """{"type": "$type", "requestId": "$requestId", "logLevel": "$logLevel", "errorCode": "${errorCode.name}", "message": "$message"}"""
            .trimMargin()
    }
}
