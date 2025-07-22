package com.yapp.yapp.audio

import org.springframework.stereotype.Component

@Component
class AudioDao(
    private val audioRepository: AudioRepository,
) {
    fun loadResource(filename: String): AudioResource {
        return audioRepository.loadResource(filename)
    }

    fun getRandomCoachAudio(): AudioResource {
        return audioRepository.getRandomCoachAudio()
    }

    fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource {
        return audioRepository.getDistanceGoalAudio(type)
    }

    fun getTimeGoalAudio(type: TimeAudioType): AudioResource {
        return audioRepository.getTimeGoalAudio(type)
    }
}
