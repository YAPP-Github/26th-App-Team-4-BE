package com.yapp.yapp.running.converter

import com.yapp.yapp.running.Pace
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Duration

@Converter(autoApply = true)
class PaceConverter : AttributeConverter<Pace, Long> {
    override fun convertToDatabaseColumn(attribute: Pace?): Long? {
        return attribute?.pacePerKm?.seconds
    }

    override fun convertToEntityAttribute(dbData: Long?): Pace? {
        return Pace(Duration.ofSeconds(dbData ?: 0L))
    }
}
