package com.yapp.yapp.common.token.jwt.annotation

import com.yapp.yapp.common.token.jwt.TokenType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authenticated(
    val tokenType: TokenType = TokenType.ACCESS,
)
