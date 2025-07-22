package com.yapp.yapp.audio

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
}
