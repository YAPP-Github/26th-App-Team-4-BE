package com.yapp.yapp.auth.infrastructure.provider.apple

import com.yapp.yapp.common.feign.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "appleApi",
    url = "\${oidc.apple.api.url}",
    configuration = [FeignConfig::class],
)
interface AppleFeignClient {
    @GetMapping("/auth/keys")
    fun fetchJwks(): String
}
