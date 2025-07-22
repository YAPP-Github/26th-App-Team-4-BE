package com.yapp.yapp.audio.infrastructure

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.yapp.yapp.audio.AudioRepository
import com.yapp.yapp.audio.AudioResource
import com.yapp.yapp.audio.DistanceAudioType
import com.yapp.yapp.audio.TimeAudioType
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
            )
        private val DISTANCE_AUDIO_PATH_MAP =
            mapOf(
                DistanceAudioType.DISTANCE_FINISH to "goals/distance/distance-finish-01.wav",
                DistanceAudioType.DISTANCE_LEFT_1KM to "goals/distance/distance-left-1km-01.wav",
                DistanceAudioType.DISTANCE_PASS_1KM to "goals/distance/distance-pass-1km-01.wav",
                DistanceAudioType.DISTANCE_PASS_2KM to "goals/distance/distance-pass-2km-01.wav",
                DistanceAudioType.DISTANCE_PASS_3KM to "goals/distance/distance-pass-3km-01.wav",
            )

        private val TIME_AUDIO_PATH_MAP =
            mapOf(
                TimeAudioType.TIME_FINISH to "goals/time/time-finish-01.wav",
                TimeAudioType.TIME_LEFT_5MIN to "goals/time/time-left-5min-01.wav",
                TimeAudioType.TIME_PASS_HALF to "goals/time/time-pass-half-01.wav",
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
                ?: throw CustomException(ErrorCode.AUDIO_NOT_FOUND)
        return getAudioResource(filePath)
    }

    override fun getTimeGoalAudio(type: TimeAudioType): AudioResource {
        val filePath =
            TIME_AUDIO_PATH_MAP[type]
                ?: throw CustomException(ErrorCode.AUDIO_NOT_FOUND)
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
