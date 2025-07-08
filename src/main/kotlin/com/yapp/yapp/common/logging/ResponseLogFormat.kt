package com.yapp.yapp.common.logging

data class ResponseLogFormat(
    val type: String,
    val requestId: String,
    val method: String,
    val uri: String,
    val status: String,
    val statusCode: Int,
    val body: String,
)
