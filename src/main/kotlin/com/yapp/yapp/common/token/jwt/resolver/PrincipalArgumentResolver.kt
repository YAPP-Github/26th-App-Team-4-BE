package com.yapp.yapp.common.token.jwt.resolver

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.common.token.jwt.RefreshPrincipal
import com.yapp.yapp.common.token.jwt.TokenType
import com.yapp.yapp.common.token.jwt.annotation.Principal
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class PrincipalArgumentResolver(
    private val tokenHandler: JwtTokenHandler,
) : HandlerMethodArgumentResolver {
    companion object {
        private const val TOKEN_TYPE = "Bearer "
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(Principal::class.java) &&
            parameter.parameterType == RefreshPrincipal::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val request =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw CustomException(ErrorCode.UNAUTHORIZED_REQUEST)
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorization == null || !authorization.startsWith(TOKEN_TYPE)) {
            throw CustomException(ErrorCode.UNAUTHORIZED_REQUEST)
        }
        val token = authorization.substring(TOKEN_TYPE.length)
        val id = tokenHandler.getTokenId(token, TokenType.REFRESH)
        val userId = tokenHandler.getUserId(token, TokenType.REFRESH)
        return RefreshPrincipal(id, userId)
    }
}
