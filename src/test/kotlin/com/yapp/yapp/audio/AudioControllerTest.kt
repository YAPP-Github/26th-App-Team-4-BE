package com.yapp.yapp.audio

import com.deepromeet.atcha.support.BaseControllerTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class AudioControllerTest : BaseControllerTest() {
    @Test
    fun `오디오 요청에 대해 정상 응답한다`() {
        // given
        val path = "test/sample.wav"

        // when
        // then
        RestAssured.given().log().all()
            .`when`().get("/api/v1/audios/$path")
            .then().log().all()
            .statusCode(200)
    }
}
