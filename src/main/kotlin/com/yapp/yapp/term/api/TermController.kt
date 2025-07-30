package com.yapp.yapp.term.api

import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.term.api.response.TermResponse
import com.yapp.yapp.term.domain.TermService
import com.yapp.yapp.term.domain.TermType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/terms")
class TermController(
    private val termService: TermService,
) {
    @GetMapping("/{termType}")
    fun getTerm(
        @PathVariable termType: TermType,
    ): ApiResponse<TermResponse> {
        return ApiResponse.success(
            termService.getTermByTermType(termType),
        )
    }
}
