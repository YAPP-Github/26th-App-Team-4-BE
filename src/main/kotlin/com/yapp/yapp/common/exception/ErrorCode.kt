package com.yapp.yapp.common.exception

import org.springframework.boot.logging.LogLevel

enum class ErrorCode(
    val status: Int,
    val errorCode: String,
    val message: String,
    val logLevel: LogLevel,
) {
    USER_NOT_FOUND(400, "USR_001", "유저가 존재하지 않습니다.", LogLevel.WARN),
}
