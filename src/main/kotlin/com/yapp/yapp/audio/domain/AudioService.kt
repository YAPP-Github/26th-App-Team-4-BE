package com.yapp.yapp.audio.domain

import com.yapp.yapp.audio.tts.TextToSpeechManager
import org.springframework.stereotype.Service

@Service
class AudioService(
    private val audioManager: AudioManager,
    private val ttsManager: TextToSpeechManager,
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

    fun getUserInfoAudio(paceMills: Long): ByteArray {
        val audioText = audioManager.getPaceAudioText(paceMills)
        return ttsManager.synthesize(audioText)
    }
}
