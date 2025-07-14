package com.yapp.yapp.home.api

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.home.api.response.HomeResponse
import com.yapp.yapp.home.domain.HomeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/v1/home")
class HomeController(
    private val homeService: HomeService,
) {
    @GetMapping
    fun getHomeScreenData(
        @CurrentUser id: Long,
        @RequestParam(required = false) targetDate: OffsetDateTime = TimeProvider.now(),
    ): ApiResponse<HomeResponse> {
        return ApiResponse.success(
            homeService.getHomeScreenData(userId = id, targetDate = targetDate),
        )
    }
}
