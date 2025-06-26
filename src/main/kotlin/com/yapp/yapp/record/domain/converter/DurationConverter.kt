package com.yapp.yapp.record.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Duration

@Converter(autoApply = true)
class DurationConverter : AttributeConverter<Duration, Long> {
    override fun convertToDatabaseColumn(attribute: Duration?): Long? {
        return attribute?.toMillis()
    }

    override fun convertToEntityAttribute(dbData: Long?): Duration? {
        return if (dbData != null) Duration.ofMillis(dbData) else null
    }
}
