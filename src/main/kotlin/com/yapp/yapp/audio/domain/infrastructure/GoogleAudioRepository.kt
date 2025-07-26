package com.yapp.yapp.audio.domain.infrastructure

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.yapp.yapp.audio.domain.AudioRepository
import com.yapp.yapp.audio.domain.AudioResource
import com.yapp.yapp.audio.domain.DistanceAudioType
import com.yapp.yapp.audio.domain.PaceAudioType
import com.yapp.yapp.audio.domain.TimeAudioType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Repository
import java.nio.channels.Channels

@Repository
class GoogleAudioRepository(
    private val storage: Storage,
    @Value("\${audio.bucket}") private val bucketName: String,
) : AudioRepository {
    companion object {
        private val COACH_AUDIO_PATHS =
            listOf(
                "coach/coach-01.wav",
                "coach/coach-02.wav",
                "coach/coach-03.wav",
                "coach/coach-04.wav",
                "coach/coach-05.wav",
                "coach/coach-06.wav",
                "coach/coach-07.wav",
            )
        private val DISTANCE_AUDIO_PATH_MAP =
            mapOf(
                DistanceAudioType.DISTANCE_FINISH to "goals/distance/distance-finish-01.wav",
                DistanceAudioType.DISTANCE_LEFT_1KM to "goals/distance/distance-left-1km-01.wav",
                DistanceAudioType.DISTANCE_PASS_1KM to "goals/distance/distance-pass-1km-01.wav",
                DistanceAudioType.DISTANCE_PASS_2KM to "goals/distance/distance-pass-2km-01.wav",
                DistanceAudioType.DISTANCE_PASS_3KM to "goals/distance/distance-pass-3km-01.wav",
                DistanceAudioType.DISTANCE_PASS_4KM to "goals/distance/distance-pass-4km-01.wav",
                DistanceAudioType.DISTANCE_PASS_5KM to "goals/distance/distance-pass-5km-01.wav",
                DistanceAudioType.DISTANCE_PASS_6KM to "goals/distance/distance-pass-6km-01.wav",
                DistanceAudioType.DISTANCE_PASS_7KM to "goals/distance/distance-pass-7km-01.wav",
                DistanceAudioType.DISTANCE_PASS_8KM to "goals/distance/distance-pass-8km-01.wav",
                DistanceAudioType.DISTANCE_PASS_9KM to "goals/distance/distance-pass-9km-01.wav",
                DistanceAudioType.DISTANCE_PASS_10KM to "goals/distance/distance-pass-10km-01.wav",
                DistanceAudioType.DISTANCE_PASS_11KM to "goals/distance/distance-pass-11km-01.wav",
                DistanceAudioType.DISTANCE_PASS_12KM to "goals/distance/distance-pass-12km-01.wav",
                DistanceAudioType.DISTANCE_PASS_13KM to "goals/distance/distance-pass-13km-01.wav",
                DistanceAudioType.DISTANCE_PASS_14KM to "goals/distance/distance-pass-14km-01.wav",
                DistanceAudioType.DISTANCE_PASS_15KM to "goals/distance/distance-pass-15km-01.wav",
                DistanceAudioType.DISTANCE_PASS_16KM to "goals/distance/distance-pass-16km-01.wav",
                DistanceAudioType.DISTANCE_PASS_17KM to "goals/distance/distance-pass-17km-01.wav",
                DistanceAudioType.DISTANCE_PASS_18KM to "goals/distance/distance-pass-18km-01.wav",
                DistanceAudioType.DISTANCE_PASS_19KM to "goals/distance/distance-pass-19km-01.wav",
                DistanceAudioType.DISTANCE_PASS_20KM to "goals/distance/distance-pass-20km-01.wav",
                DistanceAudioType.DISTANCE_PASS_21KM to "goals/distance/distance-pass-21km-01.wav",
                DistanceAudioType.DISTANCE_PASS_22KM to "goals/distance/distance-pass-22km-01.wav",
                DistanceAudioType.DISTANCE_PASS_23KM to "goals/distance/distance-pass-23km-01.wav",
                DistanceAudioType.DISTANCE_PASS_24KM to "goals/distance/distance-pass-24km-01.wav",
                DistanceAudioType.DISTANCE_PASS_25KM to "goals/distance/distance-pass-25km-01.wav",
                DistanceAudioType.DISTANCE_PASS_26KM to "goals/distance/distance-pass-26km-01.wav",
                DistanceAudioType.DISTANCE_PASS_27KM to "goals/distance/distance-pass-27km-01.wav",
                DistanceAudioType.DISTANCE_PASS_28KM to "goals/distance/distance-pass-28km-01.wav",
                DistanceAudioType.DISTANCE_PASS_29KM to "goals/distance/distance-pass-29km-01.wav",
                DistanceAudioType.DISTANCE_PASS_30KM to "goals/distance/distance-pass-30km-01.wav",
                DistanceAudioType.DISTANCE_PASS_31KM to "goals/distance/distance-pass-31km-01.wav",
                DistanceAudioType.DISTANCE_PASS_32KM to "goals/distance/distance-pass-32km-01.wav",
                DistanceAudioType.DISTANCE_PASS_33KM to "goals/distance/distance-pass-33km-01.wav",
                DistanceAudioType.DISTANCE_PASS_34KM to "goals/distance/distance-pass-34km-01.wav",
                DistanceAudioType.DISTANCE_PASS_35KM to "goals/distance/distance-pass-35km-01.wav",
                DistanceAudioType.DISTANCE_PASS_36KM to "goals/distance/distance-pass-36km-01.wav",
                DistanceAudioType.DISTANCE_PASS_37KM to "goals/distance/distance-pass-37km-01.wav",
                DistanceAudioType.DISTANCE_PASS_38KM to "goals/distance/distance-pass-38km-01.wav",
                DistanceAudioType.DISTANCE_PASS_39KM to "goals/distance/distance-pass-39km-01.wav",
                DistanceAudioType.DISTANCE_PASS_40KM to "goals/distance/distance-pass-40km-01.wav",
                DistanceAudioType.DISTANCE_PASS_41KM to "goals/distance/distance-pass-41km-01.wav",
                DistanceAudioType.DISTANCE_PASS_42KM to "goals/distance/distance-pass-42km-01.wav",
            )

        private val TIME_AUDIO_PATH_MAP =
            mapOf(
                TimeAudioType.TIME_FINISH to "goals/time/time-finish-01.wav",
                TimeAudioType.TIME_LEFT_5MIN to "goals/time/time-left-5min-01.wav",
                TimeAudioType.TIME_PASS_HALF to "goals/time/time-pass-half-01.wav",
            )

        private val PACE_AUDIO_PATH_MAP =
            mapOf(
                PaceAudioType.PACE_FAST to setOf("goals/pace/pace-fast-01.wav"),
                PaceAudioType.PACE_FAST to setOf("goals/pace/pace-fast-02.wav"),
                PaceAudioType.PACE_SLOW to setOf("goals/pace/pace-slow-01.wav"),
                PaceAudioType.PACE_SLOW to setOf("goals/pace/pace-slow-02.wav"),
                PaceAudioType.PACE_GOOD to setOf("goals/pace/pace-good-01.wav"),
                PaceAudioType.PACE_GOOD to setOf("goals/pace/pace-good-02.wav"),
                PaceAudioType.PACE_GOOD to setOf("goals/pace/pace-good-03.wav"),
                PaceAudioType.PACE_GOOD to setOf("goals/pace/pace-good-04.wav"),
                PaceAudioType.PACE_GOOD to setOf("goals/pace/pace-good-05.wav"),
            )
    }

    override fun loadResource(filePath: String): AudioResource {
        validatePath(filePath)
        val blob = getAudioBlob(filePath)
        val resource = getResource(blob)
        return AudioResource(totalSize = blob.size, contentType = blob.contentType, resource = resource)
    }

    override fun getRandomCoachAudio(): AudioResource {
        val filePath = COACH_AUDIO_PATHS.random()
        return getAudioResource(filePath)
    }

    override fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource {
        val filePath =
            DISTANCE_AUDIO_PATH_MAP[type]
                ?: throw CustomException(ErrorCode.INVALID_DISTANCE_AUDIO_TYPE)
        return getAudioResource(filePath)
    }

    override fun getTimeGoalAudio(type: TimeAudioType): AudioResource {
        val filePath =
            TIME_AUDIO_PATH_MAP[type]
                ?: throw CustomException(ErrorCode.INVALID_TIME_AUDIO_TYPE)
        return getAudioResource(filePath)
    }

    override fun getPaceGoalAudio(type: PaceAudioType): AudioResource {
        val filePath =
            PACE_AUDIO_PATH_MAP[type]?.random()
                ?: throw CustomException(ErrorCode.INVALID_PACE_AUDIO_TYPE)
        return getAudioResource(filePath)
    }

    private fun getAudioResource(filePath: String): AudioResource {
        val blob = getAudioBlob(filePath)
        val resource = getResource(blob)
        return AudioResource(totalSize = blob.size, contentType = blob.contentType, resource = resource)
    }

    private fun getAudioBlob(filePath: String): Blob {
        val blobId = BlobId.of(bucketName, filePath)
        val blob =
            storage.get(blobId)
                ?: throw CustomException(ErrorCode.AUDIO_NOT_FOUND)
        return blob
    }

    private fun getResource(blob: Blob): InputStreamResource {
        val stream = Channels.newInputStream(blob.reader())
        return InputStreamResource(stream)
    }

    private fun validatePath(filename: String) {
        if (filename.contains("..")) {
            throw CustomException(ErrorCode.INVALID_REQUEST)
        }
    }
}
