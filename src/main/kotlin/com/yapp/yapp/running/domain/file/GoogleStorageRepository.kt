package com.yapp.yapp.running.domain.file

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class GoogleStorageRepository(
    private val storage: Storage,
    @Value("\${gcp.bucket}") private val bucketName: String,
) {
    fun uploadFile(
        filePath: String,
        multipartFile: MultipartFile,
    ): File {
        try {
            val blobInfo =
                BlobInfo.newBuilder(bucketName, filePath)
                    .setContentType(multipartFile.contentType ?: "application/octet-stream")
                    .build()
            storage.create(blobInfo, multipartFile.bytes)

            // TODO 보안성 고려
            val publicUrl = "https://storage.googleapis.com/$bucketName/$filePath"
            return File(filePath = filePath, url = publicUrl)
        } catch (e: Exception) {
            println(e.message)
            throw CustomException(ErrorCode.FILE_UPLOAD_FAILED)
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return storage.delete(bucketName, filePath)
    }

    fun Blob.toFile(): File {
        return File(filePath = this.name, url = this.mediaLink)
    }
}
