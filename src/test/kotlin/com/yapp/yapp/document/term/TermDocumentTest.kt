package com.yapp.yapp.document.term

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import io.restassured.RestAssured
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import kotlin.test.Test

class TermDocumentTest : BaseDocumentTest() {
    @Test
    fun `약관을 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(
                    parameterWithName("termType").description("약관 타입 (service, private_policy, location, withdraw) 중 하나 - case insensitive"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.title").description("약관명"),
                    fieldWithPath("result.content").description("약관 내용"),
                    fieldWithPath("result.isRequired").description("약관 필수 여부"),
                )

        val filter =
            filter("term", "term-search")
                .tag(Tag.TERM_API)
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val term = termFixture.create()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .pathParam("termType", term.termType)
            .`when`()
            .get("/api/v1/terms/{termType}")
            .then()
            .statusCode(200)
    }
}
