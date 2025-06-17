package com.yapp.yapp.document

import com.deepromeet.atcha.support.fixture.UserFixture
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.user.api.request.UserCreateRequest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class SampleDocumentTest : BaseDocumentTest() {
    @Test
    fun `유저 생성 API 문서화 예시`() {
        val user = UserFixture.create()
        val request = UserCreateRequest(user.name, user.email)

        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("name").description("유저 이름"),
                    fieldWithPath("email").description("유저 이메일"),
                )

        val restDocsResponse =
            response()
                .responseBodyField(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("result").description("유저 정보"),
                    fieldWithPath("result.id").description("유저 ID"),
                    fieldWithPath("result.name").description("유저 이름"),
                    fieldWithPath("result.email").description("유저 이메일"),
                    fieldWithPath("result.isDeleted").description("유저 삭제 여부"),
                )

        val restDocsFilter =
            filter("유저 API", "유저 생성")
                .tag(Tag.MEMBER_API)
                .summary("회원 가입")
                .description("새로운 유저를 등록한다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        RestAssured.given(spec).log().all()
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/users")
            .then().log().all()
            .statusCode(201)
    }
}
