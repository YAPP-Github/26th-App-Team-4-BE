package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RecordStatus
import com.yapp.yapp.record.domain.RunningMetricsCalculator
import com.yapp.yapp.record.domain.converter.DurationConverter
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.record.domain.point.RunningPoint
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Duration
import java.time.OffsetDateTime

@Entity
@Table(name = "RUNNING_RECORDS")
class RunningRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @Column(nullable = false)
    val userId: Long = 0L,
    @Column(nullable = false)
    var totalDistance: Double = 0.0,
    @Column(nullable = false)
    @Convert(converter = DurationConverter::class)
    var totalTime: Duration = Duration.ZERO,
    @Column(nullable = false)
    var totalCalories: Int = 0,
    @Column(nullable = false)
    val startAt: OffsetDateTime = TimeProvider.now(),
    @Column(nullable = false)
    var averageSpeed: Double = 0.0,
    @Column(nullable = false)
    @Convert(converter = PaceConverter::class)
    var averagePace: Pace = Pace(0),
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var recordStatus: RecordStatus = RecordStatus.READY,
    @Column(nullable = false)
    var isDeleted: Boolean = false,
) {
    fun start() {
        this.recordStatus = RecordStatus.IN_PROGRESS
    }

    fun pause() {
        this.recordStatus = RecordStatus.PAUSE
    }

    fun finish() {
        this.recordStatus = RecordStatus.DONE
    }

    fun updateInfoByRunningPoints(runningPoints: List<RunningPoint>) {
        if (runningPoints.isEmpty()) {
            return
        }
        this.totalDistance = runningPoints.sumOf { it.distance }
        this.totalTime = runningPoints.last().totalRunningTime
        this.totalCalories = runningPoints.sumOf { it.calories }
        this.averageSpeed =
            RunningMetricsCalculator.calculateSpeedKmh(
                distanceMeter = this.totalDistance,
                seconds = this.totalTime.seconds,
            )
        this.averagePace = Pace(distanceMeter = this.totalDistance, duration = this.totalTime)
    }
}
