package com.yapp.yapp.running

import com.deepromeet.atcha.support.BaseServiceTest
import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.RecordService
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningStopRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration

class RunningTest : BaseServiceTest() {
    @Autowired
    lateinit var recordService: RecordService

    @Test
    fun `러닝을 시작한다`() {
        // given
        val request = RunningStartRequest(0.0, 0.0, TimeProvider.now().toString())

        // when
        val response = recordService.start(0L, request)

        // then
        Assertions.assertThat(response.recordId).isNotNull
    }

    @Test
    fun `러닝 기록을 업데이트 한다`() {
        // given
        val userId = 0L
        val startResponse = recordService.start(userId, RunningStartRequest(0.0, 0.0, TimeProvider.now().toString()))
        val request =
            RunningUpdateRequest(
                startResponse.recordId,
                0.01,
                0.01,
                120,
                "PT1M2.12S",
                TimeProvider.now().toString(),
            )

        // when
        val response = recordService.update(userId, request)
        // then
        Assertions.assertThat(response.id).isNotNull
    }

    @Test
    fun `러닝 기록을 연속해서 업데이트 한다`() {
        // given
        val startRequest =
            RunningStartRequest(
                lat = 37.54100,
                lon = 126.95000,
                timeStamp = "2025-06-17T17:00:00+09:00",
            )
        val userId = 0L
        val recordId = recordService.start(userId = userId, startRequest).recordId

        // 1구간당 거리 ≈ 20.847m, 페이스 453초/km → 구간 시간 = 20.847 * 0.453 ≈ 9.444s
        val updates =
            listOf(
                // 0초
                RunningUpdateRequest(recordId, 37.54100, 126.95000, 120, "PT0S", "2025-06-17T17:00:00.000+09:00"),
                // 9.444초
                RunningUpdateRequest(recordId, 37.54110, 126.95020, 123, "PT9.444S", "2025-06-17T17:00:09.444+09:00"),
                // 18.887초
                RunningUpdateRequest(recordId, 37.54120, 126.95040, 127, "PT18.887S", "2025-06-17T17:00:18.887+09:00"),
                // 28.331초
                RunningUpdateRequest(recordId, 37.54130, 126.95060, 130, "PT28.331S", "2025-06-17T17:00:28.331+09:00"),
                // 37.775초
                RunningUpdateRequest(recordId, 37.54140, 126.95080, 132, "PT37.775S", "2025-06-17T17:00:37.775+09:00"),
                // 47.218초
                RunningUpdateRequest(recordId, 37.54150, 126.95100, 135, "PT47.218S", "2025-06-17T17:00:47.218+09:00"),
            )

        // when & then
        updates.forEach { req ->
            val resp = recordService.update(userId, req)

            // ord 가 1 이면 거리 계산 스킵
            if (resp.ord == 1L) return@forEach

            // 구간 거리(고정)
            Assertions.assertThat(resp.distance)
                .isEqualTo(20.847)

            // 속도 = 거리÷시간 → 20.847m / 9.444s ≈ 2.207 m/s
            Assertions.assertThat(resp.speed)
                .isBetween(2.207, 2.208)

            // 페이스 = 453초/km → PT7M33S
            Assertions.assertThat(resp.pace)
                .isEqualTo(Duration.parse("PT7M33S"))
        }
    }

    @Test
    fun `러닝을 시작 - 중단 - 완료 한다`() {
        // given
        val userId = 0L
        val lat = 37.54100
        val lon = 126.95000
        val startAt = TimeProvider.now()

        // when
        val start = recordService.start(userId, RunningStartRequest(lat, lon, startAt.toString()))
        val recordId = start.recordId
        val maxTime = 10
        for (i in 1..maxTime) {
            recordService.update(
                userId,
                RunningUpdateRequest(
                    recordId,
                    lat + (i * 1.0 / 1000),
                    lon + (i * 1.0 / 1000),
                    120 + i,
                    "PT${i}S",
                    startAt.plusSeconds(i.toLong()).toString(),
                ),
            )
        }
        val stopTime = maxTime + 1L
        val stop = recordService.stop(userId, RunningStopRequest(recordId, startAt.plusSeconds(stopTime).toString()))
        val doneTime = stopTime + 5L
        recordService.done(userId, RunningDoneRequest(recordId, startAt.plusSeconds(doneTime).toString()))
    }
}
