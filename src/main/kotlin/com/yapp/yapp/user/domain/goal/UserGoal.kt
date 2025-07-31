package com.yapp.yapp.user.domain.goal

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class UserGoal(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    @OneToOne
    val user: User,
    @Column(nullable = true)
    var runningPurpose: String? = null,
    @Column(nullable = true)
    var weeklyRunningCount: Int? = null,
    @Convert(converter = PaceConverter::class)
    @Column(nullable = true)
    var paceGoal: Pace? = null,
    @Column(nullable = true)
    var distanceMeterGoal: Double? = null,
    @Column(nullable = true)
    var timeGoal: Long? = null,
) {
    fun updateRunningPurpose(runningGoal: String) {
        this.runningPurpose = runningGoal
    }

    fun updateWeeklyRunCount(weeklyRunCount: Int) {
        this.weeklyRunningCount = weeklyRunCount
    }

    fun updatePaceGoal(paceGoal: Pace) {
        this.paceGoal = paceGoal
    }

    fun updateDistanceMeterGoal(distanceMeterGoal: Double) {
        this.distanceMeterGoal = distanceMeterGoal
    }

    fun updateTimeGoal(timeGoal: Long) {
        this.timeGoal = timeGoal
    }

    fun isDistanceGoalAchieved(runningRecord: RunningRecord): Boolean {
        if (this.distanceMeterGoal == null) return false
        return runningRecord.totalDistance >= this.distanceMeterGoal as Double
    }

    fun isTimeGoalAchieved(runningRecord: RunningRecord): Boolean {
        if (this.timeGoal == null) return false
        return runningRecord.totalTime <= this.timeGoal as Long
    }

    fun isPaceGoalAchieved(runningRecord: RunningRecord): Boolean {
        if (this.paceGoal == null) return false
        return runningRecord.averagePace.toMills() <= this.paceGoal?.millsPerKm as Long
    }
}
