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
}
