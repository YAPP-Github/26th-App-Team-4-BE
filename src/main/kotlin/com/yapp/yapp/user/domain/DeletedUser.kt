package com.yapp.yapp.user.domain

import com.yapp.yapp.common.TimeProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.springframework.data.annotation.CreatedDate
import java.time.OffsetDateTime

@Entity
class DeletedUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,
    @Column(length = 100)
    val reason: String? = null,
    @CreatedDate
    var deletedAt: OffsetDateTime = TimeProvider.now(),
)
