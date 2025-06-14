package com.yapp.yapp.running

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.OffsetDateTime
import kotlin.time.Duration

@Entity
class RunningRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @Column(nullable = false)
    val totalDistance: Double,
    @Column(nullable = false)
    val totalRunningTime: Duration,
    @Column(nullable = false)
    val totalCalories: Int,
    @Column(nullable = false)
    val startAt: OffsetDateTime,
    @Column(nullable = false)
    val averageSpeed: Double,
)
