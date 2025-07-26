package com.yapp.yapp.running.domain.file

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class GoogleStorgeRepository(
    private val storage: Storage,
    @Value("\${gcp.bucket}") private val bucketName: String,
) {
    fun uploadFile(
        filePath: String,
        multipartFile: MultipartFile,
    ): File {
        val blobId =
            storage.create(
                BlobInfo.newBuilder(bucketName, filePath).build(),
                multipartFile.bytes,
            )
        return File(filePath = filePath, url = blobId.mediaLink)
    }

    fun deleteFile(filePath: String): Boolean {
        return storage.delete(bucketName, filePath)
    }

    fun Blob.toFile(): File {
        return File(filePath = this.name, url = this.mediaLink)
    }
}
