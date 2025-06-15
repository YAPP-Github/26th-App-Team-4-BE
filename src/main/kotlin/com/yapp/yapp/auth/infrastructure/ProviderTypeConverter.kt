package com.yapp.yapp.auth.infrastructure

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class ProviderTypeConverter : Converter<String, ProviderType> {
    override fun convert(source: String): ProviderType = ProviderType.from(source)
}
