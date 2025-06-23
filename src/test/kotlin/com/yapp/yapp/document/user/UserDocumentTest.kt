package com.yapp.yapp.document.user

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
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
import org.springframework.test.context.bean.override.mockito.MockitoBean

class UserDocumentTest : BaseDocumentTest() {
    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

    @Test
    fun `사용자 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("리프레시 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.id").description("사용자 ID"),
                    fieldWithPath("result.email").description("사용자 이메일"),
                    fieldWithPath("result.name").description("사용자 이름"),
                    fieldWithPath("result.profileImage").description("사용자 프로필"),
                )

        val restDocsFilter =
            filter("사용자 API", "조회")
                .tag(Tag.USER_API)
                .summary("사용자 조회 API")
                .description("액세스 토큰을 통해 사용자 정보를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val loginResponse = loginUser()

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().get("/api/v1/users")
            .then()
            .statusCode(200)
    }

    @Test
    fun `회원 탈퇴 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("리프레시 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()

        val restDocsFilter =
            filter("사용자 API", "회원탈퇴")
                .tag(Tag.USER_API)
                .summary("사용자 회원 탈퇴 API")
                .description("액세스 토큰을 통해 회원 탈퇴 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()
        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().delete("/api/v1/users")
            .then()
            .statusCode(204)
    }

    private fun loginUser(): LoginResponse {
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null, null)

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

        val loginResponse = objectMapper.convertValue(result, LoginResponse::class.java)
        return loginResponse
    }
}
