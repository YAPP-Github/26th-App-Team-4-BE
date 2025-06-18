package com.yapp.yapp.auth.infrastructure.provider.kakao

import com.yapp.yapp.common.feign.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

// https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc-user-info
@FeignClient(
    name = "kakaoApi",
    url = "\${oidc.kakao.api.url}",
    configuration = [FeignConfig::class],
)
interface KakaoFeignClient {
    @GetMapping("/.well-known/jwks.json")
    fun fetchJwks(): String
}
