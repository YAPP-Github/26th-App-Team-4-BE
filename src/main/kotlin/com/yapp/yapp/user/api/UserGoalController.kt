package com.yapp.yapp.user.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.user.api.request.DistanceGoalSaveRequest
import com.yapp.yapp.user.api.request.PaceGoalSaveRequest
import com.yapp.yapp.user.api.request.TimeGoalSaveRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalSaveRequest
import com.yapp.yapp.user.api.response.UserGoalResponse
import com.yapp.yapp.user.domain.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users/goals")
class UserGoalController(
    private val userService: UserService,
) {
    @GetMapping
    fun getGoals(
        @CurrentUser id: Long,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            userService.getGoal(id),
        )
    }

    @PostMapping("/weekly-run-count")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveWeeklyRunCountGoal(
        @CurrentUser id: Long,
        @RequestBody request: WeeklyRunCountGoalSaveRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.saveGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/pace")
    @ResponseStatus(HttpStatus.CREATED)
    fun savePaceGoal(
        @CurrentUser id: Long,
        @RequestBody request: PaceGoalSaveRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.saveGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/distance")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveDistanceGoal(
        @CurrentUser id: Long,
        @RequestBody request: DistanceGoalSaveRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.saveGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/time")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveTimeGoal(
        @CurrentUser id: Long,
        @RequestBody request: TimeGoalSaveRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.saveGoal(userId = id, request = request)),
        )
    }
}
