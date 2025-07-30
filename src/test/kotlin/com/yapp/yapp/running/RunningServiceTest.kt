package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.running.api.request.RunningPollingPauseRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.BaseServiceTest
import com.yapp.yapp.support.fixture.RequestFixture
import org.assertj.core.api.Assertions
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
        val user = userFixture.create()
        val request = RunningStartRequest(0.0, 0.0, TimeProvider.now().toString())

        // when
        val response = runningService.start(userId = user.id, request)

        // then
        Assertions.assertThat(response.recordId).isNotNull
    }

    @Test
    fun `러닝 기록을 업데이트 한다`() {
        // given
        val user = userFixture.create()
        val userId = user.id
        val startResponse = runningService.start(userId, RunningStartRequest(0.0, 0.0, TimeProvider.now().toString()))
        val request =
            RunningPollingUpdateRequest(
                0.01,
                0.01,
                120,
                TimeProvider.toMills(minute = 1, second = 2) + 12,
                TimeProvider.now().toString(),
            )

        // when
        val response = runningService.pollingUpdate(userId, startResponse.recordId, request)
        // then
        Assertions.assertThat(response.pointId).isNotNull
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
            listOf( // 9.444초
                RunningPollingUpdateRequest(
                    37.54110,
                    126.95020,
                    123,
                    TimeProvider.toMills(second = 9, mills = 444),
                    "2025-06-17T17:00:09.444+09:00",
                ), // 18.887초
                RunningPollingUpdateRequest(
                    37.54120,
                    126.95040,
                    127,
                    TimeProvider.toMills(second = 18, mills = 887),
                    "2025-06-17T17:00:18.887+09:00",
                ), // 28.331초
                RunningPollingUpdateRequest(
                    37.54130,
                    126.95060,
                    130,
                    TimeProvider.toMills(second = 28, mills = 331),
                    "2025-06-17T17:00:28.331+09:00",
                ), // 37.775초
                RunningPollingUpdateRequest(
                    37.54140,
                    126.95080,
                    132,
                    TimeProvider.toMills(second = 37, mills = 775),
                    "2025-06-17T17:00:37.775+09:00",
                ), // 47.218초
                RunningPollingUpdateRequest(
                    37.54150,
                    126.95100,
                    135,
                    TimeProvider.toMills(second = 47, mills = 218),
                    "2025-06-17T17:00:47.218+09:00",
                ),
            )

        // when & then
        updates.forEach { req ->
            val resp = runningService.pollingUpdate(userId, recordId, req)

            // 구간 거리(고정)
            Assertions.assertThat(resp.distance)
                .isEqualTo(20.847)
        }
    }

    @Test
    fun `러닝을 시작 - 중단 - 완료 한다`() {
        // given
        val user = userFixture.create()
        val userId = user.id
        val lat = 37.54100
        val lon = 126.95000
        val startAt = TimeProvider.now()

        // when
        val start = runningService.start(userId, RunningStartRequest(lat, lon, startAt.toString()))
        val recordId = start.recordId
        val maxTime = 10
        for (i in 1..maxTime) {
            runningService.pollingUpdate(
                userId,
                recordId,
                RunningPollingUpdateRequest(
                    lat + (i * 1.0 / 1000),
                    lon + (i * 1.0 / 1000),
                    120 + i,
                    TimeProvider.toMills(second = i),
                    startAt.plusSeconds(i.toLong()).toString(),
                ),
            )
        }
        val stopTime = maxTime + 1L
        val stop = runningService.pause(userId, recordId, RunningPollingPauseRequest(startAt.plusSeconds(stopTime).toString()))
        val done = runningService.oldDone(userId, recordId)

        // then
        val record = runningRecordManager.getRunningRecord(id = recordId, user = user)
        Assertions.assertThat(record.totalDistance).isEqualTo(done.totalRunningDistance)
    }

    @Test
    fun `러닝 포인트가 추가될 때 마다 기록이 변한다`() {
        // given
        val user = userFixture.create()
        val userId = user.id
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
                RunningPollingUpdateRequest(
                    lat = startLat + (second * 0.0001),
                    lon = startLon + (second * 0.0001),
                    heartRate = 120 + second,
                    totalRunningTime = TimeProvider.toMills(second = second),
                    timeStamp = startAt.plusSeconds(second.toLong()).toString(),
                )
            }

        // when & then
        var count = 0
        updates.forEach { request ->
            val response = runningService.pollingUpdate(userId, start.recordId, request)
            val record = runningRecordManager.getRunningRecord(id = start.recordId, user = user)
            count++

            Assertions.assertThat(record.totalDistance).isGreaterThan(14.19 * count)
        }
    }

    @Test
    fun `러닝 포인트가 없는데 완료 메서드를 호출하면 에러가 발생한다`() {
        // given
        val user = userFixture.create()
        val userId = user.id
        val startAt = TimeProvider.now()
        val start = runningService.start(userId, RunningStartRequest(0.0, 0.0, startAt.toString()))
        val recordId = start.recordId
        val request =
            RequestFixture.runningDoneRequest(
                totalDistance = 1000.0,
                totalCalories = 100,
                startAt = startAt.toString(),
                runningPoints = emptyList(),
            )

        // when & then
        Assertions.assertThatThrownBy {
            runningService.done(userId = userId, recordId = recordId, request = request, imageFile = runningFixture.multipartFile())
        }.isInstanceOf(CustomException::class.java)
    }
}
