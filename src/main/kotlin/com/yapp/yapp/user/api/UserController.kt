package com.yapp.yapp.user.api

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.response.OnboardingResponse
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getUserById(
        @CurrentUser id: Long,
    ): ApiResponse<UserResponse> {
        val user = userService.getUserById(id)
        return ApiResponse.success(user)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withdraw(
        @CurrentUser id: Long,
    ): ApiResponse<Unit> {
        userService.delete(id)
        return ApiResponse.success()
    }

    @PostMapping("/onboarding")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveOnboarding(
        @CurrentUser id: Long,
        @RequestBody request: OnboardingRequest,
    ): ApiResponse<Unit> {
        userService.saveOnboarding(id, request)
        return ApiResponse.success()
    }

    @GetMapping("/onboarding")
    fun getOnboarding(
        @CurrentUser id: Long,
    ): ApiResponse<OnboardingResponse> {
        return ApiResponse.success(
            OnboardingResponse(
                userId = id,
                answerList = userService.getOnboardings(id),
            ),
        )
    }
}
