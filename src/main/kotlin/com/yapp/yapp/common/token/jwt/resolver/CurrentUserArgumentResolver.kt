package com.yapp.yapp.common.token.jwt.resolver

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.common.token.jwt.TokenType
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserArgumentResolver(
    private val tokenHandler: JwtTokenHandler,
) : HandlerMethodArgumentResolver {
    companion object {
        private val TOKEN_TYPE = "Bearer "
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentUser::class.java) &&
            parameter.parameterType == Long::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Long {
        val request =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw CustomException(ErrorCode.INTERNAL_SERVER)
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorization == null || !authorization.startsWith(TOKEN_TYPE)) {
            throw CustomException(ErrorCode.UNAUTHORIZED_REQUEST)
        }
        val token = authorization.substring(TOKEN_TYPE.length)
        return tokenHandler.getUserId(token, TokenType.ACCESS)
    }
}
