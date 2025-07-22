package com.yapp.yapp.common.exception

import org.springframework.boot.logging.LogLevel

enum class ErrorCode(
    val status: Int,
    val errorCode: String,
    val message: String,
    val logLevel: LogLevel,
) {
    INTERNAL_SERVER(500, "SEV_001", "알 수 없는 서버 에러입니다.", LogLevel.ERROR),

    USER_NOT_FOUND(400, "USR_001", "유저가 존재하지 않습니다.", LogLevel.WARN),
    USER_ALREADY_EXISTS_WITH_ANOTHER_PROVIDER(
        400,
        "USR_002",
        "해당 이메일은 다른 소셜 로그인 계정으로 이미 가입되어 있습니다.",
        LogLevel.WARN,
    ),

    UNSUPPORTED_PROVIDER_TYPE(400, "PRV_001", "지원 하지 않는 유형 입니다.", LogLevel.WARN),
    TOKEN_EXPIRED(401, "TKN_001", "만료된 토큰 입니다.", LogLevel.WARN),
    TOKEN_INVALID(401, "TKN_002", "유효하지 않은 토큰 입니다.", LogLevel.WARN),
    TOKEN_CLAIM_MISSING(400, "TKN_003", "토큰에 필수 클레임이 누락되었습니다", LogLevel.WARN),
    TOKEN_TYPE_MISMATCH(403, "TKN_004", "토큰 타입이 올바르지 않습니다.", LogLevel.WARN),

    EXTERNAL_API_CLIENT(400, "EXT_001", "외부 API 요청이 잘못 되었습니다.", LogLevel.WARN),
    EXTERNAL_API_UNAUTHORIZED(401, "EXT_002", "외부 API에 인증을 실패 했습니다.", LogLevel.WARN),
    EXTERNAL_API_FORBIDDEN(403, "EXT_003", "외부 API에 접근 권한이 없습니다.", LogLevel.WARN),
    EXTERNAL_API_NOT_FOUND(404, "EXT_003", "찾을 수 없는 외부 API 입니다.", LogLevel.WARN),
    EXTERNAL_API_SERVER(500, "EXT_004", "외부 API 서버에 알 수 없는 에러가 발생했습니다.", LogLevel.ERROR),

    UNAUTHORIZE_REQUEST(401, "REQ_001", "유효하지 않은 요청 입니다.", LogLevel.WARN),
    UNSUPPORTED_ATTRIBUTE(400, "ATB_001", "지원 되지 않는 속성 입니다.", LogLevel.WARN),

    INVALID_REQUEST(401, "REQ_001", "유효하지 않은 요청 입니다.", LogLevel.WARN),
    NOT_FOUND_REQUEST(404, "REQ_002", "존재하지 않는 요청 입니다.", LogLevel.WARN),
    INVALID_HEADER_REQUEST(400, "REQ_003", "유효하지 않는 헤더 요청 입니다.", LogLevel.WARN),

    RECORD_NOT_FOUND(400, "REC_001", "러닝 기록이 존재하지 않습니다.", LogLevel.WARN),
    RECORD_NO_MATCHED(400, "REC_002", "러닝 기록이 유저 정보와 일치하지 않습니다.", LogLevel.WARN),
    SEARCH_TYPE_NO_MATCHED(400, "REC_003", "검색 기록 타입이 잘못됐습니다.", LogLevel.WARN),

    RECORD_TYPE_NO_MATCHED(400, "REC_004", "일치하는 기록 타입이 없습니다.", LogLevel.WARN),

    POINT_NOT_FOUND(400, "RPO_001", "러닝 포인트가 존재하지 않습니다.", LogLevel.WARN),

    AUDIO_NOT_FOUND(400, "AUD_001", "일치하는 오디오 파일이 존재하지 않습니다.", LogLevel.WARN),
    INVALID_AUDIO_DISTANCE_TYPE(400, "AUD_002", "유효하지 않은 오디오 거리 타입입니다", LogLevel.WARN),
    INVALID_AUDIO_TIME_TYPE(400, "AUD_003", "유효하지 않은 오디오 시간 타입입니다", LogLevel.WARN),

    ANSWER_NOT_FOUND(400, "ONB_001", "온보딩 답변이 존재하지 않습니다.", LogLevel.WARN),
    RUNNER_TYPE_NOT_FOUND(400, "ONB_002", "러너 타입이 존재하지 않습니다.", LogLevel.WARN),

    GOAL_NOT_FOUND(400, "GAL_001", "설정한 목표가 존재하지 않습니다.", LogLevel.WARN),
}
