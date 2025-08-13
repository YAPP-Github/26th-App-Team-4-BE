package com.yapp.yapp.common.web.converter

import com.yapp.yapp.term.domain.TermType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class TermTypeConverter : Converter<String, TermType> {
    override fun convert(source: String): TermType = TermType.from(source)
}
