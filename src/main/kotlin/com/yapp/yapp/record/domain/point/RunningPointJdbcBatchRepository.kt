package com.yapp.yapp.record.domain.point

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class RunningPointJdbcBatchRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun batchInsert(
        recordId: Long,
        points: List<RunningPoint>,
        batchSize: Int = 1000,
    ) {
        val sql =
            """
            INSERT INTO running_point (
                running_record_id,
                order_no,
                lat,
                lon,
                distance,
                pace,
                calories,
                total_running_time,
                total_running_distance,
                time_stamp,
                is_deleted
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

        points.chunked(batchSize).forEach { chunk ->
            jdbcTemplate.batchUpdate(sql, chunk, chunk.size) { ps, p ->
                ps.setLong(1, recordId)
                ps.setLong(2, p.orderNo)
                ps.setDouble(3, p.lat)
                ps.setDouble(4, p.lon)
                ps.setDouble(5, p.distance)
                ps.setLong(6, p.pace.millsPerKm)
                ps.setInt(7, p.calories)
                ps.setLong(8, p.totalRunningTime)
                ps.setDouble(9, p.totalRunningDistance)
                ps.setTimestamp(10, java.sql.Timestamp(p.timeStamp.toInstant().toEpochMilli()))
                ps.setBoolean(11, p.isDeleted)
            }
        }
    }
}
