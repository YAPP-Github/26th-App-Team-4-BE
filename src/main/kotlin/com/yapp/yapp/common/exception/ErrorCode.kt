package com.yapp.yapp.common.exception

import org.springframework.boot.logging.LogLevel

enum class ErrorCode(
    val status: Int,
    val errorCode: String,
    val message: String,
    val logLevel: LogLevel,
) {
    USER_NOT_FOUND(400, "USR_001", "유저가 존재하지 않습니다.", LogLevel.WARN),

    UNSUPPORTED_PROVIDER_TYPE(400, "PRV_001", "지원 하지 않는 유형 입니다.", LogLevel.WARN),
    TOKEN_EXPIRED(401, "TKN_001", "만료된 토큰 입니다.", LogLevel.WARN),
    TOKEN_INVALID(401, "TKN_002", "유효 하지 않은 토큰 입니다.", LogLevel.WARN),

    EXTERNAL_API_CLIENT(400, "EXT_001", "외부 API 요청이 잘못 되었습니다.", LogLevel.WARN),
    EXTERNAL_API_UNAUTHORIZED(401, "EXT_002", "외부 API에 인증을 실패 했습니다.", LogLevel.WARN),
    EXTERNAL_API_FORBIDDEN(403, "EXT_003", "외부 API에 접근 권한이 없습니다.", LogLevel.WARN),
    EXTERNAL_API_NOT_FOUND(404, "EXT_003", "찾을 수 없는 외부 API 입니다.", LogLevel.WARN),
    EXTERNAL_API_SERVER(500, "EXT_004", "외부 API 서버에 알 수 없는 에러가 발생했습니다.", LogLevel.ERROR),

    UNSUPPORTED_ATTRIBUTE(400, "ATB_001", "지원 되지 않는 속성 입니다.", LogLevel.WARN),
    INVALID_REQUEST(401, "REQ_001", "유효하지 않은 요청 입니다.", LogLevel.WARN),
}
