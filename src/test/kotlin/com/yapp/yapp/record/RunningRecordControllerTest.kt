package com.yapp.yapp.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.TimeProvider.toStartOfDay
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.running.domain.RunningService
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.DayOfWeek

class RunningRecordControllerTest : BaseControllerTest() {
    @Autowired
    lateinit var runningService: RunningService

    @Test
    fun `유저의 러닝 기록 리스트를 조회한다`() {
        // given
        val now = TimeProvider.now().toStartOfDay()
        val user = userFixture.createWithGoal()
        val otherUser = userFixture.create(email = "otherUser@email.com")

        runningFixture.createRunningRecord(user = user, startAt = now)
        runningFixture.createRunningRecord(user = otherUser, startAt = now)
        runningFixture.createRunningRecord(user = user, startAt = now.plusDays(1))

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .param("type", RecordsSearchType.ALL.name)
                .param("targetDate", now.toString())
                .param("page", 0)
                .param("size", 10)
                .`when`()
                .get("/api/v1/records")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, RunningRecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 주간 러닝 기록 리스트를 조회한다`() {
        // given
        val now = TimeProvider.now().with(DayOfWeek.MONDAY)
        val user = userFixture.createWithGoal()

        runningFixture.createRunningRecord(user = user, startAt = now)
        runningFixture.createRunningRecord(user = user, startAt = now.plusDays(2))
        runningFixture.createRunningRecord(user = user, startAt = now.minusDays(8))

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .param("type", RecordsSearchType.WEEKLY.name)
                .param("targetDate", now.toString())
                .param("page", 0)
                .param("size", 10)
                .`when`()
                .get("/api/v1/records")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, RunningRecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 연간 러닝 기록 리스트를 조회한다`() {
        // given
        val now = TimeProvider.now().toStartOfDay()
        val user = userFixture.createWithGoal()

        runningFixture.createRunningRecord(user = user, startAt = now)
        runningFixture.createRunningRecord(user = user, startAt = now.plusMonths(2))
        runningFixture.createRunningRecord(user = user, startAt = now.minusYears(1))

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .param("type", RecordsSearchType.YEARLY.name)
                .param("targetDate", now.toString())
                .param("page", 0)
                .param("size", 10)
                .`when`()
                .get("/api/v1/records")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, RunningRecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 일간 러닝 기록 리스트를 조회한다`() {
        // given
        val now = TimeProvider.now().toStartOfDay().withHour(0)
        val user = userFixture.createWithGoal()

        runningFixture.createRunningRecord(user = user, startAt = now)
        runningFixture.createRunningRecord(user = user, startAt = now.plusHours(2))
        runningFixture.createRunningRecord(user = user, startAt = now.minusHours(2))

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .param("type", RecordsSearchType.DAILY.name)
                .param("targetDate", now.toString())
                .param("page", 0)
                .param("size", 10)
                .`when`()
                .get("/api/v1/records")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, RunningRecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 러닝 기록을 XML로 조회한다`() {
        // given
        val now = TimeProvider.now().toStartOfDay()
        val user = userFixture.createWithGoal()
        val runningRecord =
            runningFixture.createRunningRecord(
                user = user,
                startAt = now,
                totalSeconds = 10L,
            )

        // when
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .accept(MediaType.APPLICATION_XML_VALUE)
            .pathParam("recordId", runningRecord.id)
            .`when`()
            .get("/api/v1/records/{recordId}")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `목표가 없는 사용자가 러닝 기록 리스트를 조회한다`() {
        // given
        val user = userFixture.create()
        val startResponse = runningService.start(userId = user.id, request = RequestFixture.runningStartRequest())
        val recordId = startResponse.recordId
        runningService.done(
            userId = user.id,
            recordId = recordId,
            request = RequestFixture.runningDoneRequest(),
            imageFile = runningFixture.multipartFile(),
        )

        // when
        // then
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .param("type", RecordsSearchType.ALL.name)
            .param("page", 0)
            .param("size", 10)
            .`when`()
            .get("/api/v1/records")
            .then().log().all()
            .statusCode(200)
    }

    @ParameterizedTest
    @CsvSource("0,10,10", "1,10,10", "2,10,5", "0,5,5")
    fun `유저의 러닝 기록 리스트를 페이지 단위로 조회한다`(
        page: Int,
        size: Int,
        expectedSize: Int,
    ) {
        // given
        val now = TimeProvider.now().toStartOfDay()
        val user = userFixture.createWithGoal()

        (1L..25L).forEach { runningFixture.createRunningRecord(user = user, startAt = now.plusDays(it)) }

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .param("type", RecordsSearchType.ALL.name)
                .param("targetDate", now.toString())
                .param("page", page)
                .param("size", size)
                .`when`()
                .get("/api/v1/records")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, RunningRecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(expectedSize)
    }
}
