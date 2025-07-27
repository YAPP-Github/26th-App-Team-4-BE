package com.yapp.yapp.document.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

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
            filter("running", "running-start")
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

//    @Test
    fun `러닝 완료 API`() {
        // given
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("totalTime").description("총 러닝 시간 (밀리초)"),
                    fieldWithPath("totalDistance").description("총 러닝 거리 (m)"),
                    fieldWithPath("totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("averagePace").description("평균 페이스 (밀리초)"),
                    fieldWithPath("startAt").description("러닝 시작 시간"),
                    fieldWithPath("runningPoints").description("러닝 포인트 리스트"),
                    fieldWithPath("runningPoints[].lat").description("러닝 포인트 위도"),
                    fieldWithPath("runningPoints[].lon").description("러닝 포인트 경도"),
                    fieldWithPath("runningPoints[].totalRunningTimeMills").description("러닝 포인트까지의 총 러닝 시간 (밀리초)"),
                    fieldWithPath("runningPoints[].timeStamp").description("러닝 포인트 기록 시간"),
                )
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.recordId").description("기록 ID"),
                    fieldWithPath("result.runningPoints").description("러닝 포인트 리스트"),
                    fieldWithPath("result.runningPoints[].pointId").description("러닝 포인트 ID"),
                    fieldWithPath("result.runningPoints[].userId").description("유저 ID"),
                    fieldWithPath("result.runningPoints[].recordId").description("기록 ID"),
                    fieldWithPath("result.runningPoints[].orderNo").description("러닝 포인트 순서"),
                    fieldWithPath("result.runningPoints[].lat").description("위도"),
                    fieldWithPath("result.runningPoints[].lon").description("경도"),
                    fieldWithPath("result.runningPoints[].speed").description("속도(km/h)"),
                    fieldWithPath("result.runningPoints[].distance").description("거리(m)"),
                    fieldWithPath("result.runningPoints[].pace").description("페이스 밀리초 단위"),
                    fieldWithPath("result.runningPoints[].heartRate").description("심박수"),
                    fieldWithPath("result.runningPoints[].calories").description("칼로리"),
                    fieldWithPath("result.runningPoints[].totalRunningTime").description("러닝 포인트 기록 당시 총 러닝 시간 밀리초 단위"),
                    fieldWithPath("result.runningPoints[].totalRunningDistance").description("러닝 포인트 기록 당시 총 러닝 거리"),
                    fieldWithPath("result.runningPoints[].timeStamp").description("러닝 포인트 기록 시간"),
                    fieldWithPath("result.segments").description("구간 기록 리스트(1km 구간별 정보)"),
                    fieldWithPath("result.segments[].orderNo").description("구간 순서"),
                    fieldWithPath(
                        "result.segments[].distanceMeter",
                    ).description("구간 거리(m). 1,000 단위로 나누어 떨어지지 않습니다. 구간 순서에 따라서 (구간 순서 + km)로 거리를 나타내는 것을 추천합니다."),
                    fieldWithPath("result.segments[].averagePace").description("구간 평균 페이스 밀리초 단위"),
                    fieldWithPath("result.totalDistance").description("총 이동 거리(m)"),
                    fieldWithPath("result.totalTime").description("총 러닝 시간 밀리초 단위"),
                    fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("result.startAt").description("시작 시간"),
                    fieldWithPath("result.averagePace").description("평균 페이스 밀리초 단위"),
                    fieldWithPath("result.imageUrl").description("러닝 경로 이미지 URL"),
                )
        val filter =
            filter("running", "running-done")
                .tag(Tag.RUNNING_API)
                .summary("러닝 완료")
                .description("러닝을 완료하는 API입니다.\n응답은 러닝 기록 단건 조회 API의 응답과 동일합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        val startResponse =
            runningService.start(
                user.id,
                RunningStartRequest(37.5665, 126.9780, "2025-06-17T16:12:03+09:00"),
            )
        val request = RequestFixture.runningDoneRequest()
        val recordId = startResponse.recordId
        val testImageFile = runningFixture.file()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .contentType("multipart/form-data")
            .multiPart("metadata", objectMapper.writeValueAsString(request), "application/json")
            .multiPart("image", testImageFile, "images/png")
            .pathParam("recordId", recordId)
            .`when`().post("/api/v1/running/{recordId}")
            .then().log().all()
            .statusCode(200)
    }

// 혼란 제거를 폴링에 대한 API 문서를 제거하겠습니다.
//
//    @Test
//    fun `러닝 폴링 업데이트 API`() {
//        // given
//        val restDocsRequest =
//            request()
//                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
//                .requestBodyField(
//                    fieldWithPath("lat").description("위도"),
//                    fieldWithPath("lon").description("경도"),
//                    fieldWithPath("heartRate").description("심박수").optional(),
//                    fieldWithPath("totalRunningTime").description("총 러닝 시간 밀리초 단위"),
//                    fieldWithPath("timeStamp").description("데이터를 기록한 시간"),
//                )
//                .requestHeader(
//                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
//                )
//        val restDocsResponse =
//            response()
//                .responseBodyFieldWithResult(
//                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
//                    fieldWithPath("result.userId").description("유저 ID"),
//                    fieldWithPath("result.runningPoint.pointId").description("러닝 포인트 ID"),
//                    fieldWithPath("result.runningPoint.userId").description("유저 ID"),
//                    fieldWithPath("result.runningPoint.recordId").description("러닝 기록 ID"),
//                    fieldWithPath("result.runningPoint.orderNo").description("러닝 포인트 순서"),
//                    fieldWithPath("result.runningPoint.lat").description("위도"),
//                    fieldWithPath("result.runningPoint.lon").description("경도"),
//                    fieldWithPath("result.runningPoint.speed").description("현재 속도 (m/s)"),
//                    fieldWithPath("result.runningPoint.distance").description("현재 총 거리 (m)"),
//                    fieldWithPath("result.runningPoint.pace").description("현재 페이스 (1km 이동하는데 걸리는 시간). 밀리초 단위"),
//                    fieldWithPath("result.runningPoint.heartRate").description("현재 심박수").optional(),
//                    fieldWithPath("result.runningPoint.calories").description("총 소모 칼로리"),
//                    fieldWithPath("result.runningPoint.timeStamp").description("데이터를 기록한 시간"),
//                    fieldWithPath("result.runningPoint.totalRunningTime").description("러닝 포인트 기록 당시 총 러닝 시간. 밀리초 단위"),
//                    fieldWithPath("result.runningPoint.totalRunningDistance").description("러닝 포인트 기록 당시 총 러닝 거리"),
//                )
//        val filter =
//            filter("running", "running-update")
//                .tag(Tag.RUNNING_API)
//                .summary("러닝 업데이트")
//                .description("러닝 기록을 업데이트하는 API입니다.")
//                .request(restDocsRequest)
//                .response(restDocsResponse)
//                .build()
//
//        val user = userFixture.create()
//        val startResponse =
//            runningService.start(
//                user.id,
//                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
//            )
//        val request = RequestFixture.runningUpdateRequest()
//        val recordId = startResponse.recordId
//
//        // when & then
//        RestAssured.given(spec).log().all()
//            .filter(filter)
//            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
//            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
//            .accept(APPLICATION_JSON_VALUE)
//            .pathParam("recordId", recordId)
//            .body(request)
//            .`when`().post("/api/v1/running/{recordId}")
//            .then().log().all()
//            .statusCode(200)
//    }
//
//    @Test
//    fun `러닝 중단 API`() {
//        // given
//        val restDocsRequest =
//            request()
//                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
//                .requestBodyField(
//                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
//                )
//                .requestHeader(
//                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
//                )
//        val restDocsResponse =
//            response()
//                .responseBodyFieldWithResult(
//                    fieldWithPath("result.userId").description("유저 ID"),
//                    fieldWithPath("result.recordId").description("러닝 기록 ID"),
//                )
//
//        val filter =
//            filter("running", "running-stop")
//                .tag(Tag.RUNNING_API)
//                .summary("러닝 중단")
//                .description("러닝 기록을 중단하는 API입니다.")
//                .request(restDocsRequest)
//                .response(restDocsResponse)
//                .build()
//
//        val user = userFixture.create()
//        val startResponse =
//            runningService.start(
//                user.id,
//                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
//            )
//        runningService.pollingUpdate(
//            user.id,
//            startResponse.recordId,
//            RunningPollingUpdateRequest(
//                37.5665,
//                126.9780,
//                142,
//                0L,
//                "2025-06-17T16:12:00+09:00",
//            ),
//        )
//        runningService.pollingUpdate(
//            user.id,
//            startResponse.recordId,
//            RunningPollingUpdateRequest(
//                37.5675,
//                126.9790,
//                140,
//                TimeProvider.toMills(second = 1),
//                "2025-06-17T16:12:01+09:00",
//            ),
//        )
//        val request = RunningPauseRequest("2025-06-17T16:12:02+09:00")
//        val recordId = startResponse.recordId
//
//        // when & then
//        RestAssured.given(spec).log().all()
//            .filter(filter)
//            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
//            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
//            .pathParam("recordId", recordId)
//            .body(request)
//            .`when`().patch("/api/v1/running/{recordId}")
//            .then().log().all()
//            .statusCode(200)
//    }
//
//    @Test
//    fun `러닝 완료 API`() {
//        // given
//        val restDocsRequest =
//            request()
//                .pathParameter(parameterWithName("recordId").description("러닝 기록 ID"))
//                .requestBodyField(
//                    fieldWithPath("timeStamp").description("러닝 중단 시간"),
//                )
//                .requestHeader(
//                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
//                )
//        val restDocsResponse =
//            response().responseBodyFieldWithResult(
//                fieldWithPath("result.recordId").description("러닝 기록 ID"),
//                fieldWithPath("result.totalRunningDistance").description("총 러닝 거리 (m)"),
//                fieldWithPath("result.totalRunningTime").description("총 러닝 시간. 밀리초 단위"),
//                fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
//                fieldWithPath("result.startAt").description("러닝 시작 시간"),
//                fieldWithPath("result.averagePace").description("평균 페이스 (1km 이동하는데 걸리는 시간). 밀리초 단위"),
//            )
//
//        val filter =
//            filter("running", "running-done")
//                .tag(Tag.RUNNING_API)
//                .summary("러닝 완료")
//                .description("러닝 기록을 완료하는 API입니다.")
//                .request(restDocsRequest)
//                .response(restDocsResponse)
//                .build()
//
//        val user = userFixture.create()
//        val startResponse =
//            runningService.start(
//                user.id,
//                RunningStartRequest(37.5665, 126.9780, TimeProvider.now().toString()),
//            )
//        runningService.pollingUpdate(
//            user.id,
//            startResponse.recordId,
//            RunningPollingUpdateRequest(
//                37.5665,
//                126.9780,
//                142,
//                0L,
//                "2025-06-17T16:12:00+09:00",
//            ),
//        )
//        runningService.pollingUpdate(
//            user.id,
//            startResponse.recordId,
//            RunningPollingUpdateRequest(
//                37.5675,
//                126.9790,
//                140,
//                TimeProvider.toMills(second = 1),
//                "2025-06-17T16:12:01+09:00",
//            ),
//        )
//        val request = RunningDoneRequest("2025-06-17T16:12:02+09:00")
//        val recordId = startResponse.recordId
//
//        // when & then
//        RestAssured.given(spec).log().all()
//            .filter(filter)
//            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
//            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
//            .pathParam("recordId", recordId)
//            .body(request)
//            .`when`().post("/api/v1/running/{recordId}/done")
//            .then().log().all()
//            .statusCode(200)
//    }
}
