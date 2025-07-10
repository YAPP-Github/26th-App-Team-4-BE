package com.yapp.yapp.user.domain.goal

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Duration

@Entity
class UserGoal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    val user: User,
    @Column(nullable = true)
    var runningPurpose: String? = null,
    @Column(nullable = true)
    var weeklyRunCount: Int? = null,
    @Convert(converter = PaceConverter::class)
    @Column(nullable = true)
    var paceGoal: Pace? = null,
    @Column(nullable = true)
    var distanceMeterGoal: Double? = null,
    @Column(nullable = true)
    var timeGoal: Duration? = null,
) {
    fun updateRunningPurpose(runningGoal: String) {
        this.runningPurpose = runningGoal
    }

    fun updateWeeklyRunCount(weeklyRunCount: Int) {
        this.weeklyRunCount = weeklyRunCount
    }

    fun updatePaceGoal(paceGoal: Pace) {
        this.paceGoal = paceGoal
    }

    fun updateDistanceMeterGoal(distanceMeterGoal: Double) {
        this.distanceMeterGoal = distanceMeterGoal
    }

    fun updateTimeGoal(timeGoal: Duration) {
        this.timeGoal = timeGoal
    }
}
