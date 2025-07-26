package com.yapp.yapp.common.token.jwt.aop

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.common.token.jwt.annotation.Authenticated
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class TokenAspect(
    private val jwtTokenHandler: JwtTokenHandler,
) {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_TYPE = "Bearer "
    }

    @Around("@annotation(authenticated)")
    fun verifyToken(
        joinPoint: ProceedingJoinPoint,
        authenticated: Authenticated,
    ): Any? {
        val attrs =
            RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
                ?: throw CustomException(ErrorCode.INVALID_REQUEST)

        val request = attrs.request
        val token = request.getHeader(AUTHORIZATION_HEADER).substring(TOKEN_TYPE.length)
        val tokenType = authenticated.tokenType

        jwtTokenHandler.getUserId(token, tokenType)

        return joinPoint.proceed()
    }
}
