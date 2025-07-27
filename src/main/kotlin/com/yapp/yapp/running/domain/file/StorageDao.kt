package com.yapp.yapp.running.domain.file

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class StorageDao(
    private val storgeRepository: GoogleStorageRepository,
) {
    fun uploadFile(
        filePath: String,
        multipartFile: MultipartFile,
    ): File {
        return storgeRepository.uploadFile(filePath = filePath, multipartFile = multipartFile)
    }

    fun deleteFile(filePath: String) {
        storgeRepository.deleteFile(filePath)
    }
}
