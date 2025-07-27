package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RecordStatus
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "RUNNING_RECORDS")
class RunningRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @Column(nullable = false)
    var totalDistance: Double = 0.0,
    @Column(nullable = false)
    var totalTime: Long = 0L,
    @Column(nullable = false)
    var totalCalories: Int = 0,
    @Column(nullable = false)
    val startAt: OffsetDateTime = TimeProvider.now(),
    @Column(nullable = false)
    @Convert(converter = PaceConverter::class)
    var averagePace: Pace = Pace(0),
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var recordStatus: RecordStatus = RecordStatus.READY,
    @Column(nullable = false, columnDefinition = "TEXT")
    var imageUrl: String = "",
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
        this.averagePace = Pace(distanceMeter = this.totalDistance, durationMills = this.totalTime)
    }

    fun update(
        totalDistance: Double? = null,
        totalTime: Long? = null,
        totalCalories: Int? = null,
        averagePace: Pace? = null,
        imageUrl: String? = null,
    ) {
        totalDistance?.let { this.totalDistance = it }
        totalTime?.let { this.totalTime = it }
        totalCalories?.let { this.totalCalories = it }
        averagePace?.let { this.averagePace = it }
        imageUrl?.let { this.imageUrl = it }
    }

    fun isOwnedBy(user: User): Boolean {
        return this.user.id == user.id
    }

    fun delete() {
        this.isDeleted = true
    }
}
