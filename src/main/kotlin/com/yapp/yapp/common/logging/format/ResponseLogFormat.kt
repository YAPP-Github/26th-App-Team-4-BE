package com.yapp.yapp.common.logging.format

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

data class ResponseLogFormat(
    val type: String = "RESPONSE",
    val requestId: String,
    val method: String,
    val uri: String,
    val status: String,
    val statusCode: Int,
    val duration: Long,
    val body: JsonNode?,
) {
    override fun toString(): String {
        val mapper = ObjectMapper()
        val jsonNode =
            mapper.createObjectNode().apply {
                put("type", type)
                put("requestId", requestId)
                put("method", method)
                put("uri", uri)
                put("status", status)
                put("statusCode", statusCode)
                put("duration", duration)
                put("body", body)
            }

        return mapper.writeValueAsString(jsonNode)
    }
}
