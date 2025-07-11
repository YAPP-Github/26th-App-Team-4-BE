package com.yapp.yapp.document.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_XML_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

class RunningDocumentTest : BaseDocumentTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Test
    fun `러닝 시작 API`() {
        // given
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("timeStamp").description("시간"),
                )
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
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

        val request = RunningStartRequest(0.0, 0.0, TimeProvider.now().toString())

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken())
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/running")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 업데이트 API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
                .requestBodyField(
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("heartRate").description("심박수").optional(),
                    fieldWithPath("totalRunningTime").description("총 러닝 시간 Duration 형식. ISO-8601 표준 문자열"),
                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
                )
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.runningPoint.id").description("러닝 포인트 ID"),
                    fieldWithPath("result.runningPoint.userId").description("유저 ID"),
                    fieldWithPath("result.runningPoint.recordId").description("러닝 기록 ID"),
                    fieldWithPath("result.runningPoint.orderNo").description("러닝 포인트 순서"),
                    fieldWithPath("result.runningPoint.lat").description("위도"),
                    fieldWithPath("result.runningPoint.lon").description("경도"),
                    fieldWithPath("result.runningPoint.speed").description("현재 속도 (m/s)"),
                    fieldWithPath("result.runningPoint.distance").description("현재 총 거리 (m)"),
                    fieldWithPath("result.runningPoint.pace").description("현재 페이스 (1km 이동하는데 걸리는 시간), 초 단위 까지 제공"),
                    fieldWithPath("result.runningPoint.heartRate").description("현재 심박수").optional(),
                    fieldWithPath("result.runningPoint.calories").description("총 소모 칼로리"),
                    fieldWithPath("result.runningPoint.timeStamp").description("데이터를 기록한 시간"),
                    fieldWithPath("result.runningPoint.totalRunningTime").description("러닝 포인트 기록 당시 총 러닝 시간"),
                    fieldWithPath("result.runningPoint.totalRunningDistance").description("러닝 포인트 기록 당시 총 러닝 거리"),
                )
        val filter =
            filter("러닝 API", "러닝 업데이트")
                .tag(Tag.RUNNING_API)
                .summary("러닝 업데이트")
                .description("러닝 기록을 업데이트하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        val startResponse =
            runningService.start(
                user.id,
                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
            )
        val request = RequestFixture.runningUpdateRequest()
        val recordId = startResponse.recordId

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("recordId", recordId)
            .body(request)
            .`when`().post("/api/v1/running/{recordId}")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 업데이트 XML API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
        val filter =
            filter("러닝 API", "러닝 업데이트 XML")
                .tag(Tag.RUNNING_API)
                .summary("러닝 업데이트")
                .description("러닝 기록을 업데이트하는 API입니다.")
                .request(restDocsRequest)
                .build()

        val user = userFixture.create()
        val startResponse =
            runningService.start(
                user.id,
                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
            )
        val request = RequestFixture.runningUpdateRequest()
        val recordId = startResponse.recordId

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_XML_VALUE)
            .pathParam("recordId", recordId)
            .body(request)
            .`when`().post("/api/v1/running/{recordId}")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 중단 API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
                .requestBodyField(
                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
                )
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
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

        val user = userFixture.create()
        val startResponse =
            runningService.start(
                user.id,
                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
            )
        runningService.update(
            user.id,
            startResponse.recordId,
            RunningUpdateRequest(
                37.5665,
                126.9780,
                142,
                0L,
                "2025-06-17T16:12:00+09:00",
            ),
        )
        runningService.update(
            user.id,
            startResponse.recordId,
            RunningUpdateRequest(
                37.5675,
                126.9790,
                140,
                TimeProvider.toMills(second = 1),
                "2025-06-17T16:12:01+09:00",
            ),
        )
        val request = RunningPauseRequest("2025-06-17T16:12:02+09:00")
        val recordId = startResponse.recordId

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .pathParam("recordId", recordId)
            .body(request)
            .`when`().patch("/api/v1/running/{recordId}")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 완료 API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
                .requestBodyField(
                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
                )
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
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

        val user = userFixture.create()
        val startResponse =
            runningService.start(
                user.id,
                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
            )
        runningService.update(
            user.id,
            startResponse.recordId,
            RunningUpdateRequest(
                37.5665,
                126.9780,
                142,
                0L,
                "2025-06-17T16:12:00+09:00",
            ),
        )
        runningService.update(
            user.id,
            startResponse.recordId,
            RunningUpdateRequest(
                37.5675,
                126.9790,
                140,
                TimeProvider.toMills(second = 1),
                "2025-06-17T16:12:01+09:00",
            ),
        )
        val request = RunningDoneRequest("2025-06-17T16:12:02+09:00")
        val recordId = startResponse.recordId

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .pathParam("recordId", recordId)
            .body(request)
            .`when`().post("/api/v1/running/{recordId}/done")
            .then().log().all()
            .statusCode(200)
    }
}
