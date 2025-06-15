package com.yapp.yapp.auth.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.token.jwt.JwtTokenGenerator
import com.yapp.yapp.common.token.jwt.JwtTokenHandler
import com.yapp.yapp.common.token.jwt.TokenResponse
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val authManager: AuthManager,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val jwtTokenHandler: JwtTokenHandler,
    private val userRepository: UserRepository,
) {
    fun login(
        providerType: ProviderType,
        token: String,
        nonce: String?,
    ): TokenResponse {
        val authUserInfo = authManager.authenticate(providerType, token, nonce)

        val email = authUserInfo.getEmail()
        val user =
            userRepository.findByEmail(email) ?: userRepository.save(
                User(
                    email = email,
                    name = UUID.randomUUID().toString(),
                ),
            )

        return jwtTokenGenerator.generateTokens(user.id)
    }

    fun logout(refreshToken: String) {
        jwtTokenHandler.expire(refreshToken)
    }

    fun refresh(refreshToken: String): TokenResponse {
        val userId = jwtTokenHandler.getUserId(refreshToken)
        jwtTokenHandler.expire(refreshToken)
        return jwtTokenGenerator.generateTokens(userId)
    }
}
