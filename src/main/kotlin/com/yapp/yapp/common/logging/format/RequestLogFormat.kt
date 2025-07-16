package com.yapp.yapp.common.logging.format

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

data class RequestLogFormat(
    val type: String = "REQUEST",
    val requestId: String,
    val method: String,
    val uri: String,
    val headers: Map<String, String> = emptyMap(),
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
                put("headers", headers["authorization"]?.let { "*****" } ?: "")
                set<JsonNode>("body", body ?: mapper.nullNode())
            }

        return mapper.writeValueAsString(jsonNode)
    }
}
