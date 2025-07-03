package com.yapp.yapp.record.domain

import java.time.Duration

class Pace(
    val pacePerKm: Duration,
) {
    companion object {
        fun List<Pace>.averagePace(): Pace {
            if (isEmpty()) return Pace(Duration.ZERO)

            val totalMillis = sumOf { it.pacePerKm.toMillis() }
            val avgMillis = totalMillis / size
            return Pace(Duration.ofMillis(avgMillis))
        }

        fun calculatePace(
            distanceMeter: Double,
            duration: Duration,
        ): Duration {
            if (distanceMeter <= 0.0) {
                return Duration.ZERO
            }

            // 2. 거리를 미터(m)에서 킬로미터(km)로 변환
            val distanceKm = distanceMeter / 1000.0

            // 3. 1km당 페이스 계산 (총 시간 / 총 거리(km))
            return Duration.ofSeconds((duration.toSeconds() / distanceKm).toLong())
        }
    }
    constructor(secondFor1Km: Long) : this(Duration.ofSeconds(secondFor1Km))

    constructor(distanceMeter: Double, duration: Duration) :
        this(calculatePace(distanceMeter, duration))

    override fun toString(): String {
        val minutes = pacePerKm.toMinutes()
        val seconds = pacePerKm.minusMinutes(minutes).seconds
        return String.format("%d:%02d /km", minutes, seconds)
    }
}
