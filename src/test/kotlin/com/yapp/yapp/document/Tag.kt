package com.yapp.yapp.document

enum class Tag(
    private val displayName: String,
) {
    USER_API("User API"),
    ERROR_API("Error API"),
    RUNNING_API("Running API"),
    AUTH_API("Auth API"),
    RECORD_API("Record API"),
    ;

    fun getDisplayName(): String {
        return displayName
    }
}
