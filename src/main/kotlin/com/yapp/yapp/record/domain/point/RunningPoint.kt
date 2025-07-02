package com.yapp.yapp.record.domain.point

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.record.domain.record.RunningRecord
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
    val orderNo: Long = 0,
    @Column(nullable = false)
    val lat: Double,
    @Column(nullable = false)
    val lon: Double,
    @Column(nullable = false)
    var speedKmh: Double = 0.0,
    @Column(nullable = false)
    var distance: Double = 0.0,
    @Column(nullable = false)
    @Convert(converter = PaceConverter::class)
    var pace: Pace = Pace(0),
    @Column(nullable = false)
    val heartRate: Int? = 0,
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
)
