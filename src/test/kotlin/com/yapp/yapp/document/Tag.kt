package com.yapp.yapp.document

enum class Tag(
    private val displayName: String,
) {
    USER_API("User API"),
    ERROR_API("Error API"),
    RUNNING_API("Running API"),
    AUTH_API("Auth API"),
    AUDIO_API("Audio API"),
    RECORD_API("Record API"),
    GOAL_API("Goal API"),
    HOME_API("Home API"),
    ;

    fun getDisplayName(): String {
        return displayName
    }
}
