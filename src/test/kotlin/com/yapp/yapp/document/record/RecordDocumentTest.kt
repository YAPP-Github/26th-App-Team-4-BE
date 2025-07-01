package com.yapp.yapp.document.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.record.domain.record.RunningRecordRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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
                .pathParameter(
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
                    fieldWithPath("result.records[].totalTime").description("총 이동 시간"),
                    fieldWithPath("result.records[].totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("result.records[].startAt").description("시작 시간"),
                    fieldWithPath("result.records[].averageSpeed").description("평균 속도"),
                    fieldWithPath("result.records[].averagePace").description("평균 페이스"),
                    fieldWithPath("result.records[].runningPoints").description("러닝 포인트 리스트"),
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.records").description("러닝 기록 리스트"),
                    fieldWithPath("result.recordCount").description("러닝 기록 개수"),
                    fieldWithPath("result.totalDistance").description("총 이동 거리"),
                    fieldWithPath("result.totalTime").description("총 이동 시간"),
                    fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("result.averageSpeed").description("평균 속도"),
                    fieldWithPath("result.averagePace").description("평균 페이스"),
                )

        val filter =
            filter("기록 API", "러닝 기록 리스트 조회")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 리스트 조회")
                .description("사용자의 러닝 기록을 페이징 조회하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val now = TimeProvider.now()
        val user = userFixture.create()

        runningFixture.createRunningRecord(userId = user.id, startAt = now)
        runningFixture.createRunningRecord(userId = user.id, startAt = now.plusDays(1))
        runningFixture.createRunningRecord(userId = user.id, startAt = now.plusDays(2))

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
                    fieldWithPath("result.totalDistance").description("총 이동 거리"),
                    fieldWithPath("result.totalTime").description("총 이동 시간"),
                    fieldWithPath("result.totalCalories").description("총 소모 칼로리"),
                    fieldWithPath("result.startAt").description("시작 시간"),
                    fieldWithPath("result.averageSpeed").description("평균 속도"),
                    fieldWithPath("result.averagePace").description("평균 페이스"),
                )

        val filter =
            filter("기록 API", "러닝 기록 단건 조회")
                .tag(Tag.RECORD_API)
                .summary("러닝 기록 단건 조회")
                .description("특정 러닝 기록의 상세 정보를 조회하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        val runningRecord = runningFixture.createRunningRecord(userId = user.id)
        val recordId = runningRecord.id

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .`when`().get("/api/v1/records/{recordId}", recordId)
            .then().log().all()
            .statusCode(200)
    }
}
