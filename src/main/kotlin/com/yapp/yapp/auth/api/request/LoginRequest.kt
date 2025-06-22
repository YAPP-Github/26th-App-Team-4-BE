package com.yapp.yapp.auth.api.request

data class LoginRequest(
    val idToken: String,
    val nonce: String?,
    val name: String?,
)
