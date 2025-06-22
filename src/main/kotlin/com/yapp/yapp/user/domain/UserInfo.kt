package com.yapp.yapp.user.domain

data class UserInfo(
    val id: Long,
    val email: String,
    val name: String,
    val profile: String,
    val isNew: Boolean = false,
)
