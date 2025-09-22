package com.yapp.yapp.document.user

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class UserDocumentTest : BaseDocumentTest() {
    @Test
    fun `사용자 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.user.userId").description("사용자 ID"),
                    fieldWithPath("result.user.email").description("사용자 이메일"),
                    fieldWithPath("result.user.nickname").description("사용자 닉네임"),
                    fieldWithPath("result.user.provider").description("소셜 로그인 유형 (카카오: KAKAO, 애플: APPLE )"),
                    fieldWithPath("result.goal").description("사용자의 목표 정보 (없을 경우 null)").optional(),
                    fieldWithPath("result.goal.goalId").description("목표 ID").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.userId").description("사용자 ID").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.runningPurpose").description(
                        "달리기 목적 (" +
                            "다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION )",
                    ).type(JsonFieldType.STRING).optional(),
                    fieldWithPath("result.goal.weeklyRunningCount").description("주간 달리기 횟수").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.paceGoal").description("페이스 목표 시간 밀리초 단위").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.distanceMeterGoal").description("거리 목표(m)").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.timeGoal").description("시간 목표 시간 밀리초 단위").type(JsonFieldType.NUMBER).optional(),
                )

        val restDocsFilter =
            filter("user", "search")
                .tag(Tag.USER_API)
                .summary("사용자 조회 API")
                .description("액세스 토큰을 통해 사용자 정보를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        val accessToken = getAccessToken(user.email)
        userGoalFixture.create(user)

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", accessToken)
            .`when`().get("/api/v1/users")
            .then()
            .statusCode(200)
    }

    @Test
    fun `회원 탈퇴 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("reason").description("회원 탈퇴 사유 (최대 100자)").optional(),
                )

        val restDocsResponse =
            response()

        val restDocsFilter =
            filter("user", "withdraw")
                .tag(Tag.USER_API)
                .summary("사용자 회원 탈퇴 API")
                .description("액세스 토큰을 통해 회원 탈퇴 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val accessToken = getAccessToken()

        val withdrawRequest = RequestFixture.withDrawRequest("러닝 목표를 달성했어요")
        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .body(withdrawRequest)
            .`when`().delete("/api/v1/users")
            .then()
            .statusCode(204)
    }
}
