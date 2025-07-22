package com.yapp.yapp.audio

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class DistanceAudioType {
    DISTANCE_PASS_1KM,
    DISTANCE_PASS_2KM,
    DISTANCE_PASS_3KM,
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
