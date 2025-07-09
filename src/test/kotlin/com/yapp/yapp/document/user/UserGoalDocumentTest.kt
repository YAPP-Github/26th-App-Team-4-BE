package com.yapp.yapp.document.user

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.user.api.request.DistanceGoalSaveRequest
import com.yapp.yapp.user.api.request.PaceGoalSaveRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalSaveRequest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class UserGoalDocumentTest : BaseDocumentTest() {
    @Test
    fun `주간 달리기 횟수 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("count").description("목표 달리기 회수"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningGoal").description("달리기 목표"),
                    fieldWithPath("result.weeklyRunCount").description("주간 달리기 횟수"),
                    fieldWithPath("result.paceGoal").description("페이스 목표(mm:ss)"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                    fieldWithPath("result.timeGoal").description("시간 목표(mm:ss)"),
                )

        val restDocsFilter =
            filter("목표 API", "주간 달리기 횟수")
                .tag(Tag.GOAL_API)
                .summary("주간 달리기 횟수 목표 설정 API")
                .description("주간 달리기 횟수 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = WeeklyRunCountGoalSaveRequest(count = 3)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/weekly-run-count")
            .then()
            .statusCode(201)
    }

    @Test
    fun `페이스 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("pace").description("목표 페이스(ISO 8601 형식)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningGoal").description("달리기 목표"),
                    fieldWithPath("result.weeklyRunCount").description("페이스"),
                    fieldWithPath("result.paceGoal").description("페이스 목표(mm:ss)"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                    fieldWithPath("result.timeGoal").description("시간 목표(mm:ss)"),
                )

        val restDocsFilter =
            filter("목표 API", "페이스")
                .tag(Tag.GOAL_API)
                .summary("페이스 목표 설정 API")
                .description("페이스 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = PaceGoalSaveRequest(pace = "PT6M30S")
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/pace")
            .then()
            .statusCode(201)
    }

    @Test
    fun `거리 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("distanceMeter").description("목표 거리(m)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningGoal").description("달리기 목표"),
                    fieldWithPath("result.weeklyRunCount").description("거리"),
                    fieldWithPath("result.paceGoal").description("거리 목표(mm:ss)"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                    fieldWithPath("result.timeGoal").description("시간 목표(mm:ss)"),
                )

        val restDocsFilter =
            filter("목표 API", "거리")
                .tag(Tag.GOAL_API)
                .summary("거리 목표 설정 API")
                .description("거리 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = DistanceGoalSaveRequest(distanceMeter = 5000.0)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/distance")
            .then()
            .statusCode(201)
    }
}
