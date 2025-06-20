package com.yapp.yapp.running.api.request

data class RunningDoneRequest(
    val userId: Long,
    val recordId: Long,
    val timeStamp: String,
)
