package com.yapp.yapp.document.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.record.domain.record.RunningRecordRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_XML_VALUE
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

class RecordDocumentTest : BaseDocumentTest() {
    @Autowired
    lateinit var runningRecordRepository: RunningRecordRepository

    @Test
    fun `러닝 기록 리스트 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .queryParameter(
                    parameterWithName("type")
                        .description("검색 타입 (ALL, TODAY 등). 기본값 ALL")
                        .optional(),
                    parameterWithName("targetDate")
                        .description("조회 기준 날짜 (ISO-8601 문자열), 기본값 현재 시각")
                        .optional(),
                    parameterWithName("page")
                        .description("페이지 번호 (0부터 시작)").optional(),
                    parameterWithName("size")
                        .description("페이지 크기").optional(),
                    parameterWithName("sort")
                        .description("정렬 기준 (ex: createdAt,DESC)").optional(),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.records[].recordId").description("러닝 기록 ID"),
                    fieldWithPath("result.records[].userId").description("유저 ID"),
                    fieldWithPath("result.records[].totalDistance").description("총 이동 거리"),
                    fieldWithPath("result.records[].totalTime").description("총 러닝 시간 밀리초 단위"),
                    fieldWithPath("result.records[].startAt").description("러닝 시작 시간"),
                    fieldWithPath("result.records[].averagePace").description("평균 페이스 밀리초 단위"),
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.records").description("러닝 기록 리스트"),
                    fieldWithPath("result.recordCount").description("러닝 기록 개수"),
                    fieldWithPath("result.totalDistance").description("총 이동 거리(m)"),
                    fieldWithPath("result.totalTime").description("총 러닝 시간 밀리초 단위"),
                    fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("result.averagePace").description("평균 페이스 밀리초 단위"),
                    fieldWithPath("result.timeGoalAchievedCount").description("시간 목표 달성 횟수"),
                    fieldWithPath("result.distanceGoalAchievedCount").description("거리 목표 달성 횟수"),
                )

        val filter =
            filter("record", "record-list-search")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 리스트 조회")
                .description("사용자의 러닝 기록을 페이징 조회하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val now = TimeProvider.now()
        val user = userFixture.createWithGoal()

        runningFixture.createRunningRecord(user = user, startAt = now, totalSeconds = 60 * 20L)
        runningFixture.createRunningRecord(user = user, startAt = now.plusDays(1), totalSeconds = 60 * 20L)
        runningFixture.createRunningRecord(user = user, startAt = now.plusDays(2), totalSeconds = 60 * 20L)

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .param("type", "ALL")
            .param("targetDate", now.toString())
            .param("page", 0)
            .param("size", 10)
            .`when`()
            .get("/api/v1/records")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 기록 단건 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .pathParameter(
                    parameterWithName("recordId").description("조회할 러닝 기록 ID"),
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
            filter("record", "record-detail-search")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 단건 조회")
                .description("특정 러닝 기록의 상세 정보를 조회하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.createWithGoal()
        val runningRecord =
            runningFixture.createRunningRecord(
                user = user,
                totalSeconds = 60 * 20L,
            )
        val recordId = runningRecord.id

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .`when`().get("/api/v1/records/{recordId}", recordId)
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 기록 단건 조회 XML API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .pathParameter(
                    parameterWithName("recordId").description("조회할 러닝 기록 ID"),
                )
        val filter =
            filter("record", "record-detail-search-XML")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 단건 조회")
                .description("특정 러닝 기록의 상세 정보를 조회하는 API입니다.")
                .request(restDocsRequest)
                .build()

        val user = userFixture.createWithGoal()
        val runningRecord =
            runningFixture.createRunningRecord(
                user = user,
                totalSeconds = 60 * 20L,
            )
        val recordId = runningRecord.id

        // when & then
        RestAssured.given(spec).log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .filter(filter)
            .accept(APPLICATION_XML_VALUE)
            .`when`().get("/api/v1/records/{recordId}", recordId)
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝 기록 삭제 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .pathParameter(
                    parameterWithName("recordId").description("러닝 기록 ID"),
                )

        val restDocsResponse =
            response()
                .responseBodyField()

        val filter =
            filter("record", "record-delete")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 삭제")
                .description("사용자의 러닝 기록을 삭제하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.createWithGoal()

        val runningRecord = runningFixture.createRunningRecord(user = user)
        val recordId = runningRecord.id

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .`when`().delete("/api/v1/records/{recordId}", recordId)
            .then().log().all()
            .statusCode(200)
    }
}
