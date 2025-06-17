package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.running.converter.PaceConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Duration
import java.time.OffsetDateTime

@Entity
@Table(name = "RUNNING_POINT")
class RunningPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "running_record_id")
    val runningRecord: RunningRecord,
    @Column(nullable = false)
    val ord: Long = 0,
    @Column(nullable = false)
    val lat: Double,
    @Column(nullable = false)
    val lon: Double,
    @Column(nullable = false)
    var speed: Double = 0.0,
    @Column(nullable = false)
    var distance: Double = 0.0,
    @Column(nullable = false)
    @Convert(converter = PaceConverter::class)
    var pace: Pace = Pace(0),
    @Column(nullable = false)
    val heartRate: Int = 0,
    @Column(nullable = false)
    val calories: Int = 0,
    @Column(nullable = false)
    val totalRunningTime: Duration = Duration.ZERO,
    @Column(nullable = false)
    var totalRunningDistance: Double = 0.0,
    @Column(nullable = false)
    val timeStamp: OffsetDateTime = TimeProvider.now(),
    @Column(nullable = false)
    val isDeleted: Boolean = false,
) {
    fun copy(
        id: Long = this.id,
        runningRecord: RunningRecord = this.runningRecord,
        ord: Long = this.ord,
        lat: Double = this.lat,
        lon: Double = this.lon,
        speed: Double = this.speed,
        distance: Double = this.distance,
        pace: Pace = this.pace,
        heartRate: Int = this.heartRate,
        calories: Int = this.calories,
        totalRunningTime: Duration = this.totalRunningTime,
        totalRunningDistance: Double = this.totalRunningDistance,
        timeStamp: OffsetDateTime = this.timeStamp,
        isDeleted: Boolean = this.isDeleted,
    ) = RunningPoint(
        id = id,
        runningRecord = runningRecord,
        ord = ord,
        lat = lat,
        lon = lon,
        speed = speed,
        distance = distance,
        pace = pace,
        heartRate = heartRate,
        calories = calories,
        totalRunningTime = totalRunningTime,
        totalRunningDistance = totalRunningDistance,
        timeStamp = timeStamp,
        isDeleted = isDeleted,
    )
}
