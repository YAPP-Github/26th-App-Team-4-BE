package com.yapp.yapp.document.home

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import java.time.DayOfWeek

class HomeDocumentTest : BaseDocumentTest() {
    @Test
    fun `홈 화면 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .pathParameter(
                    parameterWithName("targetDate")
                        .description("조회 기준 날짜 (ISO-8601 문자열), 기본값 현재 시각")
                        .optional(),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.user.userId").description("유저 ID"),
                    fieldWithPath("result.user.nickname").description("유저 닉네임"),
                    fieldWithPath("result.record.totalDistance").description("누적 총 달린 거리(m)"),
                    fieldWithPath("result.record.thisWeekRunningCount").description("이번 주 달린 횟수"),
                    fieldWithPath("result.record.recentPace").description("최근 달리기 평균 페이스(밀리초)").optional(),
                    fieldWithPath("result.record.recentDistanceMeter").description("최근 달린 거리(m)").optional(),
                    fieldWithPath("result.record.recentTime").description("최근 달린 시간(밀리초)").optional(),
                    fieldWithPath("result.userGoal.runningPurpose").description("달리기 목적").optional(),
                    fieldWithPath("result.userGoal.weeklyRunningCount").description("주간 달리기 횟수 목표").optional(),
                    fieldWithPath("result.userGoal.paceGoal").description("페이스 목표(밀리초)").optional(),
                    fieldWithPath("result.userGoal.distanceMeterGoal").description("거리 목표(m)").optional(),
                    fieldWithPath("result.userGoal.timeGoal").description("시간 목표(밀리초)").optional(),
                )

        val filter =
            filter("home", "home-screen")
                .tag(Tag.HOME_API)
                .summary("홈 화면 데이터 조회")
                .description("홈 화면에 필요한 유저 정보, 목표, 기록 요약 데이터를 조회하는 API입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // 테스트에 필요한 데이터 생성
        val user = userFixture.create()
        userGoalFixture.create(user) // 유저 목표 데이터
        val targetDate = TimeProvider.now().with(DayOfWeek.SUNDAY)
        runningFixture.createRunningRecord(user.id, startAt = targetDate.with(DayOfWeek.MONDAY))
        runningFixture.createRunningRecord(user.id, startAt = targetDate.with(DayOfWeek.THURSDAY))

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(email = user.email))
            .param("targetDate", targetDate.toString())
            .`when`()
            .get("/api/v1/home")
            .then().log().all()
            .statusCode(200)
    }
}
