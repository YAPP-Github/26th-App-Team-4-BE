package com.yapp.yapp.document.audio

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName

class AudioDocumentTest : BaseDocumentTest() {
    @Test
    fun `오디오 테스트 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Range").description("오디오 스트리밍 범위").optional(),
                )

        val filter =
            filter("오디오 API", "오디오 스트리밍")
                .tag(Tag.AUDIO_API)
                .summary("오디오 스트리밍")
                .description("오디오 파일을 스트리밍하는 테스트 API입니다.")
                .request(restDocsRequest)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .`when`().get("/api/v1/audios/test/sample.wav")
            .then().log().all()
            .statusCode(200)
    }
}
