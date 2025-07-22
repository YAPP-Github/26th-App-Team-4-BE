package com.yapp.yapp.audio.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class DistanceAudioType {
    DISTANCE_PASS_1KM,
    DISTANCE_PASS_2KM,
    DISTANCE_PASS_3KM,
    DISTANCE_PASS_4KM,
    DISTANCE_PASS_5KM,
    DISTANCE_PASS_6KM,
    DISTANCE_PASS_7KM,
    DISTANCE_PASS_8KM,
    DISTANCE_PASS_9KM,
    DISTANCE_PASS_10KM,
    DISTANCE_PASS_11KM,
    DISTANCE_PASS_12KM,
    DISTANCE_PASS_13KM,
    DISTANCE_PASS_14KM,
    DISTANCE_PASS_15KM,
    DISTANCE_PASS_16KM,
    DISTANCE_PASS_17KM,
    DISTANCE_PASS_18KM,
    DISTANCE_PASS_19KM,
    DISTANCE_PASS_20KM,
    DISTANCE_PASS_21KM,
    DISTANCE_PASS_22KM,
    DISTANCE_PASS_23KM,
    DISTANCE_PASS_24KM,
    DISTANCE_PASS_25KM,
    DISTANCE_PASS_26KM,
    DISTANCE_PASS_27KM,
    DISTANCE_PASS_28KM,
    DISTANCE_PASS_29KM,
    DISTANCE_PASS_30KM,
    DISTANCE_PASS_31KM,
    DISTANCE_PASS_32KM,
    DISTANCE_PASS_33KM,
    DISTANCE_PASS_34KM,
    DISTANCE_PASS_35KM,
    DISTANCE_PASS_36KM,
    DISTANCE_PASS_37KM,
    DISTANCE_PASS_38KM,
    DISTANCE_PASS_39KM,
    DISTANCE_PASS_40KM,
    DISTANCE_PASS_41KM,
    DISTANCE_PASS_42KM,
    DISTANCE_LEFT_1KM,
    DISTANCE_FINISH,
    ;

    companion object {
        fun getByName(name: String): DistanceAudioType {
            return DistanceAudioType.entries
                .find { it.name == name }
                ?: throw CustomException(ErrorCode.INVALID_DISTANCE_AUDIO_TYPE)
        }
    }
}
