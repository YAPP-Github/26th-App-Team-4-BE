package com.yapp.yapp.common.storage

import com.yapp.yapp.running.domain.file.File
import com.yapp.yapp.running.domain.file.StorageDao
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class StorageManager(
    private val storageDao: StorageDao,
) {
    fun uploadFile(
        filePath: String,
        multipartFile: MultipartFile,
    ): File {
        return storageDao.uploadFile(filePath = filePath, multipartFile = multipartFile)
    }

    fun deleteFile(filePath: String) {
    }
}
