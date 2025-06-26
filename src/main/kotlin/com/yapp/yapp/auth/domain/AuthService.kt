package com.yapp.yapp.auth.domain

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.token.jwt.JwtTokenGenerator
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.UserManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val authManager: AuthManager,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val jwtTokenHandler: JwtTokenHandler,
    private val userManager: UserManager,
) {
    fun login(
        provider: ProviderType,
        loginRequest: LoginRequest,
    ): LoginResponse {
        val authUserInfo =
            authManager.authenticate(provider, loginRequest.idToken, loginRequest.nonce)

        val email = authUserInfo.getEmail()
        val name = loginRequest.name ?: authUserInfo.getName()
        val profile = authUserInfo.getProfile()
        val userInfo = userManager.getUserInfo(email, name, profile, provider)

        val tokenInfo = jwtTokenGenerator.generateTokens(userInfo.id)
        val tokenResponse = TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
        val userResponse =
            UserResponse(
                userInfo.id,
                userInfo.name,
                userInfo.email,
                userInfo.profileImage,
                userInfo.provider,
            )
        return LoginResponse(tokenResponse, userResponse, userInfo.isNew)
    }

    fun logout(tokenId: String) {
        jwtTokenHandler.expire(tokenId)
    }

    fun refresh(
        tokenId: String,
        userId: Long,
    ): TokenResponse {
        jwtTokenHandler.expire(tokenId)
        val tokenInfo = jwtTokenGenerator.generateTokens(userId)
        return TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
    }
}
