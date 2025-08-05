package com.yapp.yapp.record.domain.record.goal

import com.yapp.yapp.record.domain.record.RunningRecord
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "RUNNING_RECORD_GOAL_ACHIEVES")
class RunningRecordGoalAchieve(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    val runningRecord: RunningRecord,
    @Column(nullable = false)
    var isPaceGoalAchieved: Boolean = false,
    @Column(nullable = false)
    var isDistanceGoalAchieved: Boolean = false,
    @Column(nullable = false)
    var isTimeGoalAchieved: Boolean = false,
) {
    fun achievedPaceGoal() {
        isPaceGoalAchieved = true
    }

    fun achievedDistanceGoal() {
        isDistanceGoalAchieved = true
    }

    fun achievedTimeGoal() {
        isTimeGoalAchieved = true
    }
}
