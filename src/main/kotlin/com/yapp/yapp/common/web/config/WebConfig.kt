package com.yapp.yapp.common.web.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.yapp.yapp.common.token.jwt.resolver.CurrentUserArgumentResolver
import com.yapp.yapp.common.token.jwt.resolver.PrincipalArgumentResolver
import com.yapp.yapp.common.web.converter.ProviderTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
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

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val xmlMapper =
            XmlMapper().apply {
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        converters.add(MappingJackson2XmlHttpMessageConverter(xmlMapper))
    }
}
