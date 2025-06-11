package com.yapp.yapp.document

enum class Tag(
    private val displayName: String,
) {
    MEMBER_API("Member API"),
    ;

    fun getDisplayName(): String {
        return displayName
    }
}
