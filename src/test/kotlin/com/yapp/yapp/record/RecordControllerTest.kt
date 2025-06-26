package com.yapp.yapp.record

import com.deepromeet.atcha.support.BaseControllerTest
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.api.response.RecordListResponse
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.running.domain.record.RunningRecordRepository
import com.yapp.yapp.support.fixture.RunningFixture
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders

class RecordControllerTest : BaseControllerTest() {
    @Autowired
    lateinit var runningRecordRepository: RunningRecordRepository

    @Test
    fun `유저의 러닝 기록들을 조회한다`() {
        // given
        val now = TimeProvider.now()
        val user = getSavedUser()

        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now))
        runningRecordRepository.save(RunningFixture.create(userId = 999L, startAt = now))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.plusDays(1)))

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
        val response = convert(result, RecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 주간 러닝 기록들을 조회한다`() {
        // given
        val now = TimeProvider.now()
        val user = getSavedUser()

        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.plusDays(2)))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.minusDays(8)))

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
        val response = convert(result, RecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 연간 러닝 기록들을 조회한다`() {
        // given
        val now = TimeProvider.now()
        val user = getSavedUser()

        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.plusMonths(2)))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.minusYears(1)))

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
        val response = convert(result, RecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }

    @Test
    fun `유저의 일간 러닝 기록들을 조회한다`() {
        // given
        val now = TimeProvider.now().withHour(0)
        val user = getSavedUser()

        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.plusHours(2)))
        runningRecordRepository.save(RunningFixture.create(userId = user.id, startAt = now.minusHours(2)))

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
        val response = convert(result, RecordListResponse::class.java)

        // then
        Assertions.assertThat(response.records.size).isEqualTo(2)
    }
}
