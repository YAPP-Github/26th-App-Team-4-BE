package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import kotlin.time.Duration

@Entity
@Table(name = "RUNNING_RECORD")
class RunningRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @Column(nullable = false)
    var totalDistance: Double = 0.0,
    @Column(nullable = false)
    var totalRunningTime: Duration = Duration.ZERO,
    @Column(nullable = false)
    var totalCalories: Int = 0,
    @Column(nullable = false)
    val startAt: OffsetDateTime = TimeProvider.now(),
    @Column(nullable = false)
    var averageSpeed: Double = 0.0,
)
