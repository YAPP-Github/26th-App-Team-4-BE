package com.yapp.yapp.auth.domain

interface AuthUserInfo {
    fun getEmail(): String

    fun getUserId(): Long
}
