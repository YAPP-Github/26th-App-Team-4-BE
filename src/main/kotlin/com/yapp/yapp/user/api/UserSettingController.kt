package com.yapp.yapp.user.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.user.api.request.AnalysisFeedbackUpdateRequest
import com.yapp.yapp.user.api.request.AudioCoachingUpdateRequest
import com.yapp.yapp.user.api.request.AudioFeedbackUpdateRequest
import com.yapp.yapp.user.api.request.CrewRankingUpdateRequest
import com.yapp.yapp.user.api.request.PromEventUpdateRequest
import com.yapp.yapp.user.api.request.RemindAlertUpdateRequest
import com.yapp.yapp.user.api.response.AlertSettingResponse
import com.yapp.yapp.user.api.response.RunningSettingResponse
import com.yapp.yapp.user.api.response.SettingUpdateResponse
import com.yapp.yapp.user.domain.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users/setting")
class UserSettingController(
    private val userService: UserService,
) {
    @GetMapping("/running")
    fun getRunningSetting(
        @CurrentUser id: Long,
    ): ApiResponse<RunningSettingResponse> {
        return ApiResponse.success(userService.getRunningSetting(id))
    }

    @GetMapping("/alert")
    fun getAlertSetting(
        @CurrentUser id: Long,
    ): ApiResponse<AlertSettingResponse> {
        return ApiResponse.success(userService.getAlertSetting(id))
    }

    @PatchMapping("/remind-alert")
    fun updateRemindAlert(
        @CurrentUser id: Long,
        @RequestBody request: RemindAlertUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }

    @PatchMapping("/audio-coaching")
    fun updateAudioCoaching(
        @CurrentUser id: Long,
        @RequestBody request: AudioCoachingUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }

    @PatchMapping("/audio-feedback")
    fun updateAudioFeedback(
        @CurrentUser id: Long,
        @RequestBody request: AudioFeedbackUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }

    @PatchMapping("/analysis-feedback")
    fun updateAnalysisFeedback(
        @CurrentUser id: Long,
        @RequestBody request: AnalysisFeedbackUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }

    @PatchMapping("/crew-ranking")
    fun updateCrewRanking(
        @CurrentUser id: Long,
        @RequestBody request: CrewRankingUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }

    @PatchMapping("/prom-event")
    fun updatePromEvent(
        @CurrentUser id: Long,
        @RequestBody request: PromEventUpdateRequest,
    ): ApiResponse<SettingUpdateResponse> {
        return ApiResponse.success(
            userService.updateSetting(
                userId = id,
                request = request,
            ),
        )
    }
}
