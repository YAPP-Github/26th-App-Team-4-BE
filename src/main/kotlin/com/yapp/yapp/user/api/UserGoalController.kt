package com.yapp.yapp.user.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.user.api.request.DistanceGoalRequest
import com.yapp.yapp.user.api.request.PaceGoalRequest
import com.yapp.yapp.user.api.request.RunningPurposeRequest
import com.yapp.yapp.user.api.request.TimeGoalRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalRequest
import com.yapp.yapp.user.api.response.RecommendPaceResponse
import com.yapp.yapp.user.api.response.UserGoalResponse
import com.yapp.yapp.user.domain.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
    @PostMapping("/weekly-run-count")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveWeeklyRunCountGoal(
        @CurrentUser id: Long,
        @RequestBody request: WeeklyRunCountGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/pace")
    @ResponseStatus(HttpStatus.CREATED)
    fun savePaceGoal(
        @CurrentUser id: Long,
        @RequestBody request: PaceGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/distance")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveDistanceGoal(
        @CurrentUser id: Long,
        @RequestBody request: DistanceGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/time")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveTimeGoal(
        @CurrentUser id: Long,
        @RequestBody request: TimeGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PostMapping("/purpose")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveRunningGoal(
        @CurrentUser id: Long,
        @RequestBody request: RunningPurposeRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @GetMapping
    fun getGoals(
        @CurrentUser id: Long,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            userService.getGoal(id),
        )
    }

    @PatchMapping("/weekly-run-count")
    fun updateWeeklyRunCountGoal(
        @CurrentUser id: Long,
        @RequestBody request: WeeklyRunCountGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PatchMapping("/pace")
    fun updatePaceGoal(
        @CurrentUser id: Long,
        @RequestBody request: PaceGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @GetMapping("/pace/recommend")
    fun getRecommendPace(
        @CurrentUser id: Long,
    ): ApiResponse<RecommendPaceResponse> {
        return ApiResponse.success(
            RecommendPaceResponse(
                userId = id,
                recommendPace = userService.getRecommendPace(id),
            ),
        )
    }

    @PatchMapping("/distance")
    fun updateDistanceGoal(
        @CurrentUser id: Long,
        @RequestBody request: DistanceGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PatchMapping("/time")
    fun updateTimeGoal(
        @CurrentUser id: Long,
        @RequestBody request: TimeGoalRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }

    @PatchMapping("/purpose")
    fun updateRunningGoal(
        @CurrentUser id: Long,
        @RequestBody request: RunningPurposeRequest,
    ): ApiResponse<UserGoalResponse> {
        return ApiResponse.success(
            UserGoalResponse(userService.upsertGoal(userId = id, request = request)),
        )
    }
}
