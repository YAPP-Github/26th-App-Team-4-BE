package com.yapp.yapp.audio

import org.springframework.stereotype.Component

@Component
class AudioManager(
    private val audioDao: AudioDao,
) {
    fun loadResource(filename: String): AudioResource {
        return audioDao.loadResource(filename)
    }

    fun getRandomCoachAudio(): AudioResource {
        return audioDao.getRandomCoachAudio()
    }

    fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource {
        return audioDao.getDistanceGoalAudio(type)
    }

    fun getTimeGoalAudio(type: TimeAudioType): AudioResource {
        return audioDao.getTimeGoalAudio(type)
    }

    fun getPaceAudio(type: PaceAudioType): AudioResource {
        return audioDao.getPaceAudio(type)
    }
}
