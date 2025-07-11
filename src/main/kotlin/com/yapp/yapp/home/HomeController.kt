package com.yapp.yapp.home

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.home.api.HomeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/home")
class HomeController(
    private val homeService: HomeService,
) {
    @GetMapping
    fun getHomeScreenData(
        @CurrentUser id: Long,
    ): ApiResponse<HomeResponse> {
        TODO()
    }

//    @GetMapping("/goals/{goalType}")
//    fun get
}
