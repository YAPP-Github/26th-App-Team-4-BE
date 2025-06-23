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
        providerType: ProviderType,
        loginRequest: LoginRequest,
    ): LoginResponse {
        val authUserInfo =
            authManager.authenticate(providerType, loginRequest.idToken, loginRequest.nonce)

        val email = authUserInfo.getEmail()
        val name = loginRequest.name ?: authUserInfo.getName()
        val profile = authUserInfo.getProfile()
        val user = userManager.getUserInfo(email, name, profile)

        val tokenInfo = jwtTokenGenerator.generateTokens(user.id)
        val tokenResponse = TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
        val userResponse = UserResponse(user.id, user.name, user.email, user.profileImage)
        return LoginResponse(tokenResponse, userResponse, user.isNew)
    }

    fun logout(refreshToken: String) {
        jwtTokenHandler.expire(refreshToken)
    }

    fun refresh(refreshToken: String): TokenResponse {
        val userId = jwtTokenHandler.getUserId(refreshToken)
        jwtTokenHandler.expire(refreshToken)
        val tokenInfo = jwtTokenGenerator.generateTokens(userId)
        return TokenResponse(tokenInfo.accessToken, tokenInfo.refreshToken)
    }
}
