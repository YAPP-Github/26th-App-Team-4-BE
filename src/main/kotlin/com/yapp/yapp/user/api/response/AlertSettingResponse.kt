package com.yapp.yapp.user.api.response

data class AlertSettingResponse(
    val remindAlert: Boolean,
    val analysisFeedback: Boolean,
    val crewRanking: Boolean,
    val promEvent: Boolean,
)
