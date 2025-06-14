package com.yapp.yapp.running

import com.yapp.yapp.running.converter.PaceConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.OffsetDateTime

@Entity
class RunningPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "running_record_id")
    val runningRecord: RunningRecord,
    @Column(nullable = false)
    val order: Long,
    @Column(nullable = false)
    val lat: Double,
    @Column(nullable = false)
    val lon: Double,
    @Column(nullable = false)
    val speed: Double,
    @Column(nullable = false)
    val distance: Double,
    @Column(nullable = false)
    @Convert(converter = PaceConverter::class)
    val pace: Pace,
    @Column(nullable = false)
    val heartRate: Int?,
    @Column(nullable = false)
    val calories: Int? = null,
    @Column(nullable = false)
    val timestamp: OffsetDateTime,
)
