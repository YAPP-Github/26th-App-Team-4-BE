package com.yapp.yapp.running.audio

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.channels.Channels

@Service
class AudioService(
    private val storage: Storage,
    @Value("\${audio.bucket}") private val bucketName: String,
) {
    fun loadBlob(filename: String): Pair<Blob, Resource>? {
        validatePath(filename)
        val blobId = BlobId.of(bucketName, filename)
        val blob = storage.get(blobId) ?: return null
        val stream = Channels.newInputStream(blob.reader())
        val resource = InputStreamResource(stream)
        return blob to resource
    }

    private fun validatePath(filename: String) {
        if (filename.contains("..")) {
            throw IllegalArgumentException("Invalid path sequence in filename: $filename")
        }
    }
}
