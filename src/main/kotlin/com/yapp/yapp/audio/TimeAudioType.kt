package com.yapp.yapp.audio

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class TimeAudioType {
    TIME_PASS_HALF,
    TIME_LEFT_5MIN,
    TIME_FINISH,
    ;

    companion object {
        fun getByName(name: String): TimeAudioType {
            return TimeAudioType.entries
                .find { it.name == name }
                ?: throw CustomException(ErrorCode.INVALID_AUDIO_DISTANCE_TYPE)
        }
    }
}
