package com.yapp.yapp.record.domain

import com.yapp.yapp.common.TimeProvider
import java.time.Duration

class Pace(
    val millsPerKm: Long, // 1km 당 소요 시간(밀리초)
) {
    companion object {
        fun calculatePace(
            distanceMeter: Double,
            duration: Duration,
        ): Long {
            if (distanceMeter <= 0.0 || duration.isZero) {
                return 0L
            }
            // 2. 거리를 미터(m)에서 킬로미터(km)로 변환
            val distanceKm = distanceMeter / 1000.0

            // 3. 1km당 페이스 계산 (총 시간 / 총 거리(km))
            return (duration.toMillis() / distanceKm).toLong()
        }
    }
    constructor() : this(0L)

    constructor(distanceMeter: Double, duration: Duration) :
        this(calculatePace(distanceMeter, duration))

    override fun toString(): String {
        val seconds = TimeProvider.millsToSecond(toMills())
        return String.format("%d:%02d /km", seconds / 60, seconds % 60)
    }

    fun toMills(): Long {
        return millsPerKm
    }
}
