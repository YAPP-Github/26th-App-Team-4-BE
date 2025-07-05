package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.BaseServiceTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RunningServiceTest : BaseServiceTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Autowired
    lateinit var runningRecordManager: RunningRecordManager

    @Autowired
    lateinit var runningPointManger: RunningPointManger

    @Test
    fun `러닝을 시작한다`() {
        // given
        val request = RunningStartRequest(0.0, 0.0, TimeProvider.now().toString())

        // when
        val response = runningService.start(0L, request)

        // then
        Assertions.assertThat(response.recordId).isNotNull
    }

    @Test
    fun `러닝 기록을 업데이트 한다`() {
        // given
        val userId = 0L
        val startResponse = runningService.start(userId, RunningStartRequest(0.0, 0.0, TimeProvider.now().toString()))
        val request =
            RunningUpdateRequest(
                0.01,
                0.01,
                120,
                "PT1M2.12S",
                TimeProvider.now().toString(),
            )

        // when
        val response = runningService.update(userId, startResponse.recordId, request)
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
        val userId = userFixture.create().id
        val recordId = runningService.start(userId = userId, startRequest).recordId

        val updates =
            listOf(
                // 9.444초
                RunningUpdateRequest(37.54110, 126.95020, 123, "PT9.444S", "2025-06-17T17:00:09.444+09:00"),
                // 18.887초
                RunningUpdateRequest(37.54120, 126.95040, 127, "PT18.887S", "2025-06-17T17:00:18.887+09:00"),
                // 28.331초
                RunningUpdateRequest(37.54130, 126.95060, 130, "PT28.331S", "2025-06-17T17:00:28.331+09:00"),
                // 37.775초
                RunningUpdateRequest(37.54140, 126.95080, 132, "PT37.775S", "2025-06-17T17:00:37.775+09:00"),
                // 47.218초
                RunningUpdateRequest(37.54150, 126.95100, 135, "PT47.218S", "2025-06-17T17:00:47.218+09:00"),
            )

        // when & then
        updates.forEach { req ->
            val resp = runningService.update(userId, recordId, req)

            // 구간 거리(고정)
            Assertions.assertThat(resp.distance)
                .isEqualTo(20.847)

            Assertions.assertThat(resp.speed)
                .isBetween(2.207, 2.208)
        }
    }

    @Test
    fun `러닝을 시작 - 중단 - 완료 한다`() {
        // given
        val userId = userFixture.create().id
        val lat = 37.54100
        val lon = 126.95000
        val startAt = TimeProvider.now()

        // when
        val start = runningService.start(userId, RunningStartRequest(lat, lon, startAt.toString()))
        val recordId = start.recordId
        val maxTime = 10
        for (i in 1..maxTime) {
            runningService.update(
                userId,
                recordId,
                RunningUpdateRequest(
                    lat + (i * 1.0 / 1000),
                    lon + (i * 1.0 / 1000),
                    120 + i,
                    "PT${i}S",
                    startAt.plusSeconds(i.toLong()).toString(),
                ),
            )
        }
        val stopTime = maxTime + 1L
        val stop = runningService.pause(userId, recordId, RunningPauseRequest(startAt.plusSeconds(stopTime).toString()))
        val doneTime = stopTime + 5L
        val done = runningService.done(userId, recordId, RunningDoneRequest(startAt.plusSeconds(doneTime).toString()))

        // then
        val record = runningRecordManager.getRunningRecord(userId, recordId)
        Assertions.assertThat(record.totalDistance).isEqualTo(done.totalRunningDistance)
    }

    @Test
    fun `러닝 포인트가 추가될 때 마다 기록이 변한다`() {
        // given
        val userId = userFixture.create().id
        val startLat = 37.54100
        val startLon = 126.95000
        val startAt = TimeProvider.now()

        val start =
            runningService.start(
                userId,
                RunningStartRequest(startLat, startLon, startAt.toString()),
            )

        val updates =
            (1..5).map { second ->
                RunningUpdateRequest(
                    lat = startLat + (second * 0.0001),
                    lon = startLon + (second * 0.0001),
                    heartRate = 120 + second,
                    totalRunningTime = "PT${second}S",
                    timeStamp = startAt.plusSeconds(second.toLong()).toString(),
                )
            }

        // when & then
        var count = 0
        updates.forEach { request ->
            val response = runningService.update(userId, start.recordId, request)
            val record = runningRecordManager.getRunningRecord(userId, start.recordId)
            count++

            Assertions.assertThat(record.totalDistance).isGreaterThan(14.19 * count)
            Assertions.assertThat(record.averageSpeed).isCloseTo(51.1, within(0.1))
        }
    }
}
