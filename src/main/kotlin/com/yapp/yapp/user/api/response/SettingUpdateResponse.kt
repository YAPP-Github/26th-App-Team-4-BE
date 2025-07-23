package com.yapp.yapp.user.api.response

sealed interface SettingUpdateResponse

data class AudioCoachingUpdateResponse(val audioCoaching: Boolean) : SettingUpdateResponse

data class RemindAlertUpdateResponse(val remindAlert: Boolean) : SettingUpdateResponse

data class AudioFeedbackUpdateResponse(val audioFeedback: Boolean) : SettingUpdateResponse

data class AnalysisFeedbackUpdateResponse(val analysisFeedback: Boolean) : SettingUpdateResponse

data class CrewRankingUpdateResponse(val crewRanking: Boolean) : SettingUpdateResponse

data class PromEventUpdateResponse(val promEvent: Boolean) : SettingUpdateResponse
