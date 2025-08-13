package com.yapp.yapp.user.api.request

sealed interface SettingUpdateRequest

data class AudioCoachingUpdateRequest(val audioCoaching: Boolean) : SettingUpdateRequest

data class RemindAlertUpdateRequest(val remindAlert: Boolean) : SettingUpdateRequest

data class AudioFeedbackUpdateRequest(val audioFeedback: Boolean) : SettingUpdateRequest
