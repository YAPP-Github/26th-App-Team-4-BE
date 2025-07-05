package com.yapp.yapp.common.web.config

import com.yapp.yapp.auth.infrastructure.ProviderTypeConverter
import com.yapp.yapp.common.token.jwt.resolver.CurrentUserArgumentResolver
import com.yapp.yapp.common.token.jwt.resolver.PrincipalArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val currentUserArgumentResolver: CurrentUserArgumentResolver,
    private val principalArgumentResolver: PrincipalArgumentResolver,
) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(ProviderTypeConverter())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentUserArgumentResolver)
        resolvers.add(principalArgumentResolver)
    }
}
