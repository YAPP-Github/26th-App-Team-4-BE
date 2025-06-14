package com.yapp.yapp.running

import com.deepromeet.atcha.support.BaseServiceTest
import com.yapp.yapp.running.request.RunningStartRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RunningTest : BaseServiceTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Test
    fun `러닝을 시작한다`() {
        // given
        val request = RunningStartRequest(0L, 0.0, 0.0)

        // when
        val response = runningService.start(request)

        // then
        Assertions.assertThat(response.recordId).isNotNull
    }
}
