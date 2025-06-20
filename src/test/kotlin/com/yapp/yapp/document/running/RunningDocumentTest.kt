package com.yapp.yapp.document.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.running.RunningService
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningResumeRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningStopRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import java.time.OffsetDateTime

class RunningDocumentTest : BaseDocumentTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Test
    fun `러닝 시작 API`() {
        // given
        val request = RunningStartRequest(0L, 0.0, 0.0, TimeProvider.now().toString())
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("timeStamp").description("시간"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
                )
        val filter =
            filter("러닝 API", "러닝 시작")
                .tag(Tag.RUNNING_API)
                .summary("러닝 시작")
                .description("러닝을 시작하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/running/start")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 업데이트 API`() {
        // given
        val startResponse =
            runningService.start(
                RunningStartRequest(0L, 37.5665, 126.9780, OffsetDateTime.now().toString()),
            )

        val request =
            RunningUpdateRequest(
                0L,
                startResponse.recordId,
                37.5665,
                126.9780,
                142,
                "PT1M2.12S",
                "2025-06-17T16:12:00+09:00",
            )
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("recordId").description("러닝 기록 ID"),
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("heartRate").description("심박수").optional(),
                    fieldWithPath("totalRunningTime").description("총 러닝 시간 Duration 형식. ISO-8601 표준 문자열"),
                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("러닝 포인트 ID"),
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
                    fieldWithPath("result.ord").description("러닝 포인트 순서"),
                    fieldWithPath("result.lat").description("위도"),
                    fieldWithPath("result.lon").description("경도"),
                    fieldWithPath("result.speed").description("현재 속도 (m/s)"),
                    fieldWithPath("result.distance").description("현재 총 거리 (m)"),
                    fieldWithPath("result.pace").description("현재 페이스 (1km 이동하는데 걸리는 시간), 초 단위 까지 제공"),
                    fieldWithPath("result.heartRate").description("현재 심박수").optional(),
                    fieldWithPath("result.calories").description("총 소모 칼로리"),
                    fieldWithPath("result.timeStamp").description("데이터를 기록한 시간"),
                )
        val filter =
            filter("러닝 API", "러닝 업데이트")
                .tag(Tag.RUNNING_API)
                .summary("러닝 업데이트")
                .description("러닝 기록을 업데이트하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/running/update")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 중단 API`() {
        // given
        val userId = 0L
        val startResponse =
            runningService.start(
                RunningStartRequest(userId, 37.5665, 126.9780, OffsetDateTime.now().toString()),
            )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5665,
                126.9780,
                142,
                "PT0S",
                "2025-06-17T16:12:00+09:00",
            ),
        )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5675,
                126.9790,
                140,
                "PT1S",
                "2025-06-17T16:12:01+09:00",
            ),
        )

        val request = RunningStopRequest(userId, startResponse.recordId, "2025-06-17T16:12:02+09:00")
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("recordId").description("러닝 기록 ID"),
                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
                )

        val filter =
            filter("러닝 API", "러닝 중단")
                .tag(Tag.RUNNING_API)
                .summary("러닝 중단")
                .description("러닝 기록을 중단하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().patch("/api/v1/running/stop")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 재개 API`() {
        // given
        val userId = 0L
        val startResponse =
            runningService.start(
                RunningStartRequest(userId, 37.5665, 126.9780, OffsetDateTime.now().toString()),
            )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5665,
                126.9780,
                142,
                "PT0S",
                "2025-06-17T16:12:00+09:00",
            ),
        )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5675,
                126.9790,
                140,
                "PT1S",
                "2025-06-17T16:12:01+09:00",
            ),
        )
        runningService.stop(RunningStopRequest(userId, startResponse.recordId, "2025-06-17T16:12:02+09:00"))

        val request =
            RunningResumeRequest(
                userId,
                startResponse.recordId,
                37.505793,
                127.109205,
                80,
                "PT1S",
                "2025-06-17T16:15:00+09:00",
            )
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("recordId").description("러닝 기록 ID"),
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("heartRate").description("심박수").optional(),
                    fieldWithPath("totalRunningTime").description("총 러닝 시간 Duration 형식. ISO-8601 표준 문자열"),
                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("러닝 포인트 ID"),
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
                    fieldWithPath("result.ord").description("러닝 포인트 순서"),
                    fieldWithPath("result.lat").description("위도"),
                    fieldWithPath("result.lon").description("경도"),
                    fieldWithPath("result.speed").description("현재 속도 (m/s)"),
                    fieldWithPath("result.distance").description("현재 총 거리 (m)"),
                    fieldWithPath("result.pace").description("현재 페이스 (1km 이동하는데 걸리는 시간), 초 단위 까지 제공"),
                    fieldWithPath("result.heartRate").description("현재 심박수").optional(),
                    fieldWithPath("result.calories").description("총 소모 칼로리"),
                    fieldWithPath("result.timeStamp").description("데이터를 기록한 시간"),
                )

        val filter =
            filter("러닝 API", "러닝 재개")
                .tag(Tag.RUNNING_API)
                .summary("러닝 재개")
                .description("러닝 기록을 재개하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/running/resume")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 완료 API`() {
        // given
        val userId = 0L
        val startResponse =
            runningService.start(
                RunningStartRequest(userId, 37.5665, 126.9780, OffsetDateTime.now().toString()),
            )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5665,
                126.9780,
                142,
                "PT0S",
                "2025-06-17T16:12:00+09:00",
            ),
        )
        runningService.update(
            RunningUpdateRequest(
                userId,
                startResponse.recordId,
                37.5675,
                126.9790,
                140,
                "PT1S",
                "2025-06-17T16:12:01+09:00",
            ),
        )

        val request = RunningDoneRequest(userId, startResponse.recordId, "2025-06-17T16:12:02+09:00")
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("recordId").description("러닝 기록 ID"),
                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
                )
        val restDocsResponse =
            response().responseBodyFieldWithResult(
                fieldWithPath("result.recordId").description("러닝 기록 ID"),
                fieldWithPath("result.totalRunningDistance").description("총 러닝 거리 (m)"),
                fieldWithPath("result.totalRunningTime").description("총 러닝 시간 Duration 형식. ISO-8601 표준 문자열"),
                fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
                fieldWithPath("result.startAt").description("러닝 시작 시간"),
                fieldWithPath("result.averageSpeed").description("평균 속도 (m/s)"),
                fieldWithPath("result.averagePace").description("평균 페이스 (1km 이동하는데 걸리는 시간), 초 단위까지 제공"),
            )

        val filter =
            filter("러닝 API", "러닝 완료")
                .tag(Tag.RUNNING_API)
                .summary("러닝 완료")
                .description("러닝 기록을 완료하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/running/done")
            .then().log().all()
            .statusCode(200)
    }
}
