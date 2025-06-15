package com.yapp.yapp.common.exception

import org.springframework.boot.logging.LogLevel

enum class ErrorCode(
    val status: Int,
    val errorCode: String,
    val message: String,
    val logLevel: LogLevel,
) {
    USER_NOT_FOUND(400, "USR_001", "유저가 존재하지 않습니다.", LogLevel.WARN),
    TOKEN_EXPIRED(401, "TKN_001", "만료된 토큰 입니다.", LogLevel.WARN),
    TOKEN_INVALID(401, "TKN_002", "유효 하지 않은 토큰 입니다.", LogLevel.WARN),
}
