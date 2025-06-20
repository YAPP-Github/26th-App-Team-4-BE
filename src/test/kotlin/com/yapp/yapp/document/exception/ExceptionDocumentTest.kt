package com.yapp.yapp.document.exception

import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class ExceptionDocumentTest : BaseDocumentTest() {
    @Test
    fun `커스텀 예외가 발생한다`() {
        // given
        val restDocsResponse =
            response()
                .responseBodyFieldWithError(
                    fieldWithPath("code").description("에러 코드"),
                    fieldWithPath("message").description("에러 메시지").optional(),
                    fieldWithPath("timeStamp").description("응답 시간"),
                )
        val restDocsFilter =
            filter("에러", "에러 응답")
                .tag(Tag.ERROR_API)
                .summary("에러 응답")
                .description("에러를 응답 형식에 대해 설명합니다")
                .response(restDocsResponse)
                .build()

        // when
        // then
        RestAssured.given(spec).log().all()
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .`when`().get("/exception")
            .then().log().all()
            .statusCode(ErrorCode.USER_NOT_FOUND.status)
    }
}
