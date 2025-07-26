package com.yapp.yapp.document.audio

import com.yapp.yapp.audio.domain.DistanceAudioType
import com.yapp.yapp.audio.domain.PaceAudioType
import com.yapp.yapp.audio.domain.TimeAudioType
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

class AudioDocumentTest : BaseDocumentTest() {
    @Test
    fun `오디오 코치용 API`() {
        // given
        val restDocsRequest =
            request()

        val filter =
            filter("audio", "coach")
                .tag(Tag.AUDIO_API)
                .summary("오디오 코칭 API")
                .description(
                    "러너 타입에 설정된 구간 통과 시 마다 자세나 호흡 등에 대한 코칭 오디오를 조회하는 API입니다.\n" +
                        "(초급 러너: 1km, 중급 러너: 3km, 고급 러너: 3km)\n" +
                        "명세서에 적혀있는 조건에 도달할 때 마다 호출 하시면 됩니다.\n" +
                        "조회 시 마다 랜덤으로 오디오 파일을 조회합니다.",
                )
                .request(restDocsRequest)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .`when`().get("/api/v1/audios/coach")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `오디오 거리 피드백 API`() {
        // given
        val restDocsRequest =
            request()
                .queryParameter(
                    parameterWithName("type")
                        .description(
                            "거리 오디오 타입 (DISTANCE_LEFT_1KM, DISTANCE_FINISH, DISTANCE_PASS_1KM ~ DISTANCE_PASS_42KM).",
                        ),
                )

        val filter =
            filter("audio", "feedback-distance")
                .tag(Tag.AUDIO_API)
                .summary("오디오 거리 피드백 API")
                .description(
                    "거리 목표로 달리는 사용자에게 1km 마다 받을 오디오 피드백 API입니다.\n" +
                        "명세서에 적혀있는 조건에 도달할 때 마다 호출 하시면 됩니다.\n" +
                        "조회 시 마다 랜덤으로 오디오 파일을 조회합니다.",
                )
                .request(restDocsRequest)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .param("type", DistanceAudioType.DISTANCE_PASS_1KM.name)
            .`when`().get("/api/v1/audios/goals/distance")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `오디오 시간 피드백 API`() {
        // given
        val restDocsRequest =
            request()
                .queryParameter(
                    parameterWithName("type")
                        .description("시간 오디오 타입 (TIME_LEFT_5MIN, TIME_PASS_HALF, TIME_FINISH)."),
                )

        val filter =
            filter("audio", "feedback-time")
                .tag(Tag.AUDIO_API)
                .summary("오디오 시간 피드백 API")
                .description(
                    "시간 목표로 달리는 사용자에게 1km 마다 받을 오디오 피드백 API입니다.\n" +
                        "명세서에 적혀있는 조건에 도달할 때 마다 호출 하시면 됩니다.\n" +
                        "조회 시 마다 랜덤으로 오디오 파일을 조회합니다.",
                )
                .request(restDocsRequest)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .param("type", TimeAudioType.TIME_PASS_HALF.name)
            .`when`().get("/api/v1/audios/goals/time")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `오디오 페이스 피드백 API`() {
        // given
        val restDocsRequest =
            request()
                .queryParameter(
                    parameterWithName("type")
                        .description("페이스 오디오 타입 (PACE_FAST, PACE_GOOD, PACE_SLOW)."),
                )

        val filter =
            filter("audio", "feedback-pace")
                .tag(Tag.AUDIO_API)
                .summary("오디오 페이스 피드백 API")
                .description(
                    "목표 페이스에 대한 오디오 피드백 API입니다.\n" +
                        "명세서에 적혀있는 조건에 도달할 때 마다 호출 하시면 됩니다.\n" +
                        "조회 시 마다 랜덤으로 오디오 파일을 조회합니다.",
                )
                .request(restDocsRequest)
                .build()

        // when & then
        RestAssured.given(spec).log().all()
            .filter(filter)
            .param("type", PaceAudioType.PACE_GOOD.name)
            .`when`().get("/api/v1/audios/goals/pace")
            .then().log().all()
            .statusCode(200)
    }
}
