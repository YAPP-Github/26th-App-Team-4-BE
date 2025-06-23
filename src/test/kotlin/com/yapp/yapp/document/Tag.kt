package com.yapp.yapp.document

enum class Tag(
    private val displayName: String,
) {
    MEMBER_API("Member API"),
    ERROR_API("Error API"),
    RUNNING_API("Running API"),
    AUTH_API("Auth API"),
    ;

    fun getDisplayName(): String {
        return displayName
    }
}
