package com.yapp.yapp.running

import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class RunningControllerTest : BaseControllerTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Test
    fun `러닝을 폴링으로 업데이트한다`() {
        // given
        val user = userFixture.create()
        val startResponse = runningService.start(user.id, RequestFixture.runningStartRequest())
        val recordId = startResponse.recordId
        val request =
            RequestFixture.runningPollingUpdateRequest(
                heartRate = 100,
            )

        // when
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .pathParam("recordId", recordId)
            .`when`().post("/api/v1/running/{recordId}/polling")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `러닝을 일괄로 업데이트 한다`() {
        val user = userFixture.create()
        val startResponse = runningService.start(user.id, RequestFixture.runningStartRequest())
        val recordId = startResponse.recordId
        val request = RequestFixture.runningDoneRequest()

        // when
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .contentType(APPLICATION_JSON_VALUE)
            .body(request)
            .pathParam("recordId", recordId)
            .`when`().post("/api/v1/running/{recordId}")
            .then().log().all()
            .statusCode(200)
    }
}
