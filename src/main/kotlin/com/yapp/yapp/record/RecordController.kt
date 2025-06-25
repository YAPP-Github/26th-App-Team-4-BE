package com.yapp.yapp.record

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/records")
class RecordController {
    @GetMapping
    fun getRecords(
        @CurrentUser userId: Long,
    ) {
        TODO("전체, 월간, 주간, 일간 기록을 가져온다")
    }

    @GetMapping("{recordId}")
    fun getRecord(
        @CurrentUser userId: Long,
    ) {
    }
}
