package com.yapp.yapp.audio.domain

import org.springframework.stereotype.Service

@Service
class AudioService(
    private val audioManager: AudioManager,
) {
    fun loadResource(filename: String): AudioResource {
        return audioManager.loadResource(filename)
    }

    fun getCoachAudio(): AudioResource {
        return audioManager.getRandomCoachAudio()
    }

    fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource {
        return audioManager.getDistanceGoalAudio(type)
    }

    fun getTimeGoalAudio(type: TimeAudioType): AudioResource {
        return audioManager.getTimeGoalAudio(type)
    }

    fun getPaceGoalAudio(type: PaceAudioType): AudioResource {
        return audioManager.getPaceGoalAudio(type)
    }
}
