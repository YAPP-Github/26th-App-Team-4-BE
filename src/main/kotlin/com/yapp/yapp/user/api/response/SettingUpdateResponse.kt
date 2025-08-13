package com.yapp.yapp.user.api.response

sealed interface SettingUpdateResponse

data class AudioCoachingUpdateResponse(val audioCoaching: Boolean) : SettingUpdateResponse

data class RemindAlertUpdateResponse(val remindAlert: Boolean) : SettingUpdateResponse

data class AudioFeedbackUpdateResponse(val audioFeedback: Boolean) : SettingUpdateResponse
