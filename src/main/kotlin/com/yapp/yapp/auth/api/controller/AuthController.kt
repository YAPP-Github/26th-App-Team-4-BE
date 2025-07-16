package com.yapp.yapp.auth.api.controller

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.domain.AuthService
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.token.jwt.RefreshPrincipal
import com.yapp.yapp.common.token.jwt.annotation.Principal
import com.yapp.yapp.common.web.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login/{provider}")
    fun login(
        @PathVariable(name = "provider") providerType: String,
        @RequestBody loginRequest: LoginRequest,
    ): ApiResponse<LoginResponse> {
        val tokenResponse = authService.login(ProviderType.from(providerType), loginRequest)
        return ApiResponse.success(tokenResponse)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @Principal principal: RefreshPrincipal,
    ) {
        ApiResponse.success(authService.logout(principal.id))
    }

    @PostMapping("/refresh")
    fun reissueToken(
        @Principal principal: RefreshPrincipal,
    ): ApiResponse<TokenResponse> {
        return ApiResponse.success(authService.refresh(principal.id, principal.userId))
    }
}
