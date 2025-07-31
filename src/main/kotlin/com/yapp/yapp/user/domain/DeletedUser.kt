package com.yapp.yapp.user.domain

import com.yapp.yapp.common.TimeProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import java.time.OffsetDateTime

@Entity
class DeletedUser(
    @Id
    val id: Long,
    @Column(length = 100)
    val reason: String? = null,
    @CreatedDate
    var deletedAt: OffsetDateTime = TimeProvider.now(),
)
