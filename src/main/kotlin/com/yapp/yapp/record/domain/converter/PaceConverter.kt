package com.yapp.yapp.record.domain.converter

import com.yapp.yapp.record.domain.Pace
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class PaceConverter : AttributeConverter<Pace, Long> {
    override fun convertToDatabaseColumn(attribute: Pace?): Long? {
        return attribute?.toMills()
    }

    override fun convertToEntityAttribute(dbData: Long?): Pace? {
        return Pace(dbData ?: 0L)
    }
}
