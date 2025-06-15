package com.yapp.yapp.common.token.jwt

interface TokenBlacklist {
    fun add(token: String)

    fun contains(token: String): Boolean
}
