package com.yapp.yapp.document.user

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class UserSettingDocumentTest : BaseDocumentTest() {
    @Test
    fun `러닝 오디오 설정 정보 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.audioCoaching").description("오디오 코칭 설정 여부"),
                    fieldWithPath("result.audioFeedback").description("오디오 피드백 설정 여부"),
                )

        val restDocsFilter =
            filter("setting", "audio-setting")
                .tag(Tag.SETTING_API)
                .summary("러닝 오디오 설정 정보 API")
                .description("러닝 오디오 설정을 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = "test@test.com"))
            .`when`().get("/api/v1/users/setting/running")
            .then()
            .statusCode(200)
    }

    @Test
    fun `알림 설정 정보 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.remindAlert").description("루틴 리마인드 설정 여부"),
                )

        val restDocsFilter =
            filter("setting", "alert-setting")
                .tag(Tag.SETTING_API)
                .summary("알림 설정 정보 API")
                .description("알림 설정 정보를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = "test@test.com"))
            .`when`().get("/api/v1/users/setting/alert")
            .then()
            .statusCode(200)
    }

    @Test
    fun `오디오 코칭 설정 업데이트 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("audioCoaching").description("오디오 코칭 설정"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.audioCoaching").description("오디오 코칭 설정 갱신 결과"),
                )

        val restDocsFilter =
            filter("setting", "audio-coaching-update")
                .tag(Tag.SETTING_API)
                .summary("오디오 코칭 설정 업데이트 API")
                .description("오디오 코칭 설정을 업데이트 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when
        // then
        val request = RequestFixture.audioCoachingUpdateRequest()
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = "test@test.com"))
            .body(request)
            .`when`().patch("/api/v1/users/setting/audio-coaching")
            .then()
            .statusCode(200)
    }

    @Test
    fun `루틴 리마인드 설정 업데이트 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("remindAlert").description("루틴 리마인드 설정"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.remindAlert").description("루틴 리마인드 설정 갱신 결과"),
                )

        val restDocsFilter =
            filter("setting", "routine-remind-update")
                .tag(Tag.SETTING_API)
                .summary("루틴 리마인드 설정 업데이트 API")
                .description("루틴 리마인드 설정 정보를 업데이트 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when
        // then
        val request = RequestFixture.remindAlertUpdateRequest()
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = "test@test.com"))
            .body(request)
            .`when`().patch("/api/v1/users/setting/remind-alert")
            .then()
            .statusCode(200)
    }

    @Test
    fun `오디오 피드백 설정 업데이트 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("audioFeedback").description("오디오 피드백 설정"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.audioFeedback").description("오디오 피드백 설정 갱신 결과"),
                )

        val restDocsFilter =
            filter("setting", "audio-feedback-update")
                .tag(Tag.SETTING_API)
                .summary("오디오 피드백 설정 업데이트 API")
                .description("오디오 피드백 설정 정보를 업데이트 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        // when
        // then
        val request = RequestFixture.audioFeedbackUpdateRequest()
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = "test@test.com"))
            .body(request)
            .`when`().patch("/api/v1/users/setting/audio-feedback")
            .then()
            .statusCode(200)
    }
}
