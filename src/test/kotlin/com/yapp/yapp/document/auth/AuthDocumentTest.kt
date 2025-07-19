package com.yapp.yapp.document.auth

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.common.web.ApiResponse
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
    lateinit var appleFeignClient: AppleFeignClient

    @Test
    fun `로그인 API`() {
        // given
        val restDocsRequest =
            request()
                .requestBodyField(
                    fieldWithPath("idToken").description("소셜 로그인 ID 토큰"),
                    fieldWithPath("nonce").description("소셜 로그인용 nonce 값").optional(),
                )
                .pathParameter(
                    parameterWithName("provider").description("로그인 클라이언트 (apple, kakao 중 하나)"),
                )

        val restDocsResponse =
            response()
                .responseBodyField(
                    fieldWithPath("result.tokenResponse.accessToken").description("액세스 토큰"),
                    fieldWithPath("result.tokenResponse.refreshToken").description("리프레시 토큰"),
                    fieldWithPath("result.user.userId").description("사용자 ID"),
                    fieldWithPath("result.user.email").description("사용자 이메일"),
                    fieldWithPath("result.user.nickname").description("사용자 닉네임"),
                    fieldWithPath("result.user.provider").description("소셜 로그인 유형"),
                    fieldWithPath("result.isNew").description("신규 가입 여부"),
                )

        val restDocsFilter =
            filter("auth", "social-login")
                .tag(Tag.AUTH_API)
                .summary("소셜 로그인 API")
                .description("소셜 로그인을 통해 토큰 정보(액세스, 리프레시)와 사용자 정보를 받습니다")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null)

        // when
        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        // when & then
        RestAssured.given(spec)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(loginRequest)
            .pathParam("provider", "apple")
            .filter(restDocsFilter)
            .`when`()
            .post("/api/v1/auth/login/{provider}")
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
            filter("auth", "logout")
                .tag(Tag.AUTH_API)
                .summary("로그아웃 API")
                .description("발급 받았던 토큰을 무효화하며 로그아웃합니다")
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
            filter("auth", "token-reissue")
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
        val loginRequest = LoginRequest(idToken, null)

        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        return objectMapper.convertValue(result, LoginResponse::class.java).tokenResponse
    }
}
