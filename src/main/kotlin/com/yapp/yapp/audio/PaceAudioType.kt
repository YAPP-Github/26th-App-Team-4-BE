package com.yapp.yapp.audio

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class PaceAudioType {
    PACE_GOOD,
    PACE_SLOW,
    PACE_FAST,
    ;

    companion object {
        fun getByName(name: String): PaceAudioType {
            return PaceAudioType.entries
                .find { it.name == name }
                ?: throw CustomException(ErrorCode.INVALID_PACE_AUDIO_TYPE)
        }
    }
}
