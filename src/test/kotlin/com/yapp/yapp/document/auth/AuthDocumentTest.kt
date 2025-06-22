package com.yapp.yapp.document.auth

import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.IdTokenFixture
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.context.bean.override.mockito.MockitoBean

class AuthDocumentTest : BaseDocumentTest() {
    @MockitoBean
    lateinit var kakaoFeignClient: KakaoFeignClient

    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

    @Test
    fun `로그인 API`() {
        // given
        val restDocsRequest =
            request()
                .pathParameter(
                    parameterWithName("provider").description("소셜 로그인 제공자 (KAKAO, APPLE)"),
                )
                .queryParameter(
                    parameterWithName("idToken").description("소셜 로그인 ID 토큰"),
                    parameterWithName("nonce").description("소셜 로그인용 nonce 값").optional(),
                )

        val restDocsResponse =
            response()
                .responseBodyField(
                    fieldWithPath("result.accessToken").description("액세스 토큰"),
                    fieldWithPath("result.refreshToken").description("리프레시 토큰"),
                )

        val restDocsFilter =
            filter("인증 API", "소셜 로그인")
                .tag(Tag.AUTH_API)
                .summary("소셜 로그인 API")
                .description("소셜 로그인을 통해 액세스 토큰과 리프레시 토큰을 발급받습니다")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()

        // when
        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        // when & then
        RestAssured.given(spec)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .param("idToken", idToken)
            .pathParam("provider", "apple")
            .filter(restDocsFilter)
            .`when`()
            .get("/api/v1/auth/login/{provider}")
            .then()
            .statusCode(200)
    }

    @Test
    fun `로그아웃 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("리프레시 토큰 (Bearer)"),
                )

        val restDocsFilter =
            filter("인증 API", "로그아웃")
                .tag(Tag.AUTH_API)
                .summary("로그아웃 API")
                .description("리프레시 토큰을 무효화하여 로그아웃합니다")
                .request(restDocsRequest)
                .build()

        val tokenResponse = loginUser()

        // when & then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${tokenResponse.refreshToken}")
            .`when`()
            .post("/api/v1/auth/logout")
            .then()
            .statusCode(204)
    }

    @Test
    fun `토큰 재발급 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("리프레시 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyField(
                    fieldWithPath("result.accessToken").description("액세스 토큰"),
                    fieldWithPath("result.refreshToken").description("리프레시 토큰"),
                )

        val restDocsFilter =
            filter("인증 API", "토큰 재발급")
                .tag(Tag.AUTH_API)
                .summary("토큰 재발급 API")
                .description("리프레시 토큰을 통해 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val tokenResponse = loginUser()

        // when & then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${tokenResponse.refreshToken}")
            .`when`()
            .post("/api/v1/auth/refresh")
            .then()
            .statusCode(200)
    }

    private fun loginUser(): TokenResponse {
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()

        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .param("idToken", idToken)
                .`when`().get("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        return objectMapper.convertValue(result, TokenResponse::class.java)
    }
}
