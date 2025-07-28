package com.yapp.yapp.auth.domain

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.token.jwt.JwtTokenGenerator
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.UserManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val authManager: AuthManager,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val jwtTokenHandler: JwtTokenHandler,
    private val userManager: UserManager,
) {
    @Transactional
    fun login(
        provider: ProviderType,
        loginRequest: LoginRequest,
    ): LoginResponse {
        val authUserInfo =
            authManager.authenticate(provider, loginRequest.idToken, loginRequest.nonce)

        val email = authUserInfo.getEmail()
        val userInfo = userManager.getUserInfo(email, provider)

        if (userInfo.provider != provider) {
            throw CustomException(ErrorCode.USER_ALREADY_EXISTS_WITH_ANOTHER_PROVIDER)
        }

        val tokenInfo = jwtTokenGenerator.generateTokens(userInfo.id)
        val tokenResponse = TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
        val userResponse = UserResponse(userInfo)
        return LoginResponse(tokenResponse, userResponse, userInfo.isNew)
    }

    @Transactional
    fun logout(tokenId: String) {
        jwtTokenHandler.expire(tokenId)
    }

    @Transactional
    fun refresh(
        tokenId: String,
        userId: Long,
    ): TokenResponse {
        jwtTokenHandler.expire(tokenId)
        val tokenInfo = jwtTokenGenerator.generateTokens(userId)
        return TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
    }
}
