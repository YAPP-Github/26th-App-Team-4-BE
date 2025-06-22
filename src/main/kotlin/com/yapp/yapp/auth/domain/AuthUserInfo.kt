package com.yapp.yapp.auth.domain

interface AuthUserInfo {
    fun getEmail(): String

    fun getProfile(): String?

    fun getName(): String?
}
