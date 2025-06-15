package com.yapp.yapp.auth.api.controller

import com.yapp.yapp.auth.domain.AuthService
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.token.jwt.TokenResponse
import com.yapp.yapp.common.token.jwt.annotation.Token
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @GetMapping("/login/{provider}")
    fun login(
        @PathVariable(name = "provider") providerType: ProviderType,
        @RequestParam(name = "idToken") token: String,
        @RequestParam(name = "nonce", required = false) nonce: String?,
    ): ApiResponse<TokenResponse> {
        val tokenResponse = authService.login(providerType, token, nonce)
        return ApiResponse.success(tokenResponse)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @Token refreshToken: String,
    ) {
        ApiResponse.success(authService.logout(refreshToken))
    }

    @PostMapping("/refresh")
    fun reissueToken(
        @Token refreshToken: String,
    ): ApiResponse<TokenResponse> {
        return ApiResponse.success(authService.refresh(refreshToken))
    }
}
