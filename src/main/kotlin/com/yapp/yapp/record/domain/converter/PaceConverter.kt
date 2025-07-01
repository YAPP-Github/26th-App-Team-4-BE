package com.yapp.yapp.record.domain.converter

import com.yapp.yapp.record.domain.Pace
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Duration

@Converter(autoApply = true)
class PaceConverter : AttributeConverter<Pace, Long> {
    override fun convertToDatabaseColumn(attribute: Pace?): Long? {
        return attribute?.pacePerKm?.toMillis()
    }

    override fun convertToEntityAttribute(dbData: Long?): Pace? {
        return Pace(Duration.ofSeconds(dbData ?: 0L))
    }
}
