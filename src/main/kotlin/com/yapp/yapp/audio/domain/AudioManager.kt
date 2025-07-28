package com.yapp.yapp.audio.domain

import com.yapp.yapp.record.domain.Pace
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

    fun getPaceGoalAudio(type: PaceAudioType): AudioResource {
        return audioDao.getPaceGoalAudio(type)
    }

    fun getPaceAudioText(paceMills: Long): String {
        val pace = Pace(paceMills)
        return "현재 페이스는 ${pace.toTts()}입니다."
    }
}
