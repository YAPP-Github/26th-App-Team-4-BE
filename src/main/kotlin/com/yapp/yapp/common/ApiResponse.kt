package com.yapp.yapp.common

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val code: String,
    val result: T? = null,
    val timeStamp: LocalDateTime? = null,
    val message: String? = null,
) {
    companion object {
        fun <T> success(result: T): ApiResponse<T> = ApiResponse(code = "SUCCESS", result = result)

        fun error(
            code: String,
            message: String,
        ): ApiResponse<Unit> =
            ApiResponse(
                code = code,
                timeStamp = LocalDateTime.now(),
                message = message,
            )
    }
}
