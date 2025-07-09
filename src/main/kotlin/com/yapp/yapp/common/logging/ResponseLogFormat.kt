package com.yapp.yapp.common.logging

data class ResponseLogFormat(
    val type: String = "RESPONSE",
    val requestId: String,
    val method: String,
    val uri: String,
    val status: String,
    val statusCode: Int,
    val duration: Long,
    val body: String,
) {
    override fun toString(): String {
        return """{"type": "$type", "requestId": "$requestId", "method": "$method", "uri": "$uri", "status": "$status", """ +
            """"statusCode": $statusCode, "duration": $duration, "body": "$body"}""".trimMargin()
    }
}
