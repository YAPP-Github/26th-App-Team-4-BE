package com.yapp.yapp.audio.domain

import org.springframework.core.io.Resource
import org.springframework.http.MediaType

data class AudioResource(
    val totalSize: Long,
    val contentType: MediaType?,
    val resource: Resource,
) {
    constructor(totalSize: Long, contentType: String, resource: Resource) :
        this(totalSize, MediaType.parseMediaType(contentType), resource)
}
