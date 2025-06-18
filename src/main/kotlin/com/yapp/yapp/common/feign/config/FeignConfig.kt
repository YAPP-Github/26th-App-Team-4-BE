package com.yapp.yapp.common.feign.config

import com.yapp.yapp.common.feign.CustomErrorDecoder
import feign.Request
import feign.Retryer
import feign.codec.ErrorDecoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableFeignClients(basePackages = ["com.yapp.yapp"])
class FeignConfig {
    @Bean
    fun requestOptions(): Request.Options {
        return Request.Options(Duration.ofMillis(1000), Duration.ofMillis(3000), true)
    }

    @Bean
    fun retryer(): Retryer = Retryer.Default(100, 1000, 3)

    @Bean
    fun errorDecoder(): ErrorDecoder = CustomErrorDecoder()
}
