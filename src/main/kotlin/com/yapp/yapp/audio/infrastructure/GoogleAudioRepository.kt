package com.yapp.yapp.audio.infrastructure

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.yapp.yapp.audio.AudioRepository
import com.yapp.yapp.audio.AudioResource
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
    }

    override fun getRandomCoachAudio(): AudioResource {
        val filePath = COACH_AUDIO_PATHS.random()
        val blob = getAudioBlob(filePath)
        val resource = getResource(blob)
        return AudioResource(totalSize = blob.size, contentType = blob.contentType, resource = resource)
    }

    override fun loadResource(filePath: String): AudioResource {
        validatePath(filePath)
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
