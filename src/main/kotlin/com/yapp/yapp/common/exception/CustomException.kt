package com.yapp.yapp.common.exception

class CustomException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
