package com.yapp.yapp.running.domain.file

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.TimeUnit

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
            val blobInfo = BlobInfo.newBuilder(bucketName, filePath).build()
            storage.create(blobInfo, multipartFile.bytes)

            val url =
                storage.signUrl(
                    blobInfo,
                    15, // 만료 시간
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.withV4Signature(),
                )
            return File(filePath = filePath, url = url.toString())
        } catch (e: Exception) {
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
