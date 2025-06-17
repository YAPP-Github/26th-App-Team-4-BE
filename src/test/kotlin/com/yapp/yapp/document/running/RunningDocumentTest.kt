package com.yapp.yapp.document.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.running.RunningService
import com.yapp.yapp.running.api.request.RunningStartRequest
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
                .responseBodyField(
                    fieldWithPath("recordId").description("러닝 기록 ID"),
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
        val startResponse = runningService.start(RunningStartRequest(0L, 0.0, 0.0, OffsetDateTime.now().toString()))

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
                    fieldWithPath("heartRate").description("심박수"),
                    fieldWithPath("totalRunningTime").description("총 러닝 시간 Duration 형식. ISO-8601 표준 문자열"),
                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
                )
        val restDocsResponse =
            response()
                .responseBodyField(
                    fieldWithPath("id").description("러닝 포인트 ID"),
                    fieldWithPath("userId").description("유저 ID"),
                    fieldWithPath("recordId").description("러닝 기록 ID"),
                    fieldWithPath("ord").description("러닝 포인트 순서"),
                    fieldWithPath("lat").description("위도"),
                    fieldWithPath("lon").description("경도"),
                    fieldWithPath("speed").description("현재 속도 (m/s)"),
                    fieldWithPath("distance").description("현재 총 거리 (m)"),
                    fieldWithPath("pace").description("현재 페이스 (1km 이동하는데 걸리는 시간), 초 단위 까지 제공"),
                    fieldWithPath("heartRate").description("현재 심박수"),
                    fieldWithPath("calories").description("총 소모 칼로리"),
                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
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
}
