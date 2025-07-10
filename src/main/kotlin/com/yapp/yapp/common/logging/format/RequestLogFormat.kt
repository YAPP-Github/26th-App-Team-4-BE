package com.yapp.yapp.common.logging.format

data class RequestLogFormat(
    val type: String = "REQUEST",
    val requestId: String,
    val method: String,
    val uri: String,
    val headers: Map<String, String> = emptyMap(),
    val body: Any?,
) {
    override fun toString(): String {
        return """{"type": "$type", "requestId": "$requestId", "method": "$method", "uri": "$uri", """" +
            """authorization": "${headers["authorization"]}", "body": $body}"""
    }
}
