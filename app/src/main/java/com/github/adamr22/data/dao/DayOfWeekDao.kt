package com.github.adamr22.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.github.adamr22.data.entities.DayOfWeek

@Dao
interface DayOfWeekDao {
    @Query("INSERT INTO schedule (day, schedule_owner_id) VALUES (:day, :alarmId)")
    suspend fun insertSchedule(day: String, alarmId: Int)

    @Query("DELETE FROM schedule WHERE day = :day AND schedule_owner_id = :alarmId")
    suspend fun deleteDayFromSchedule(day: String, alarmId: Int)

    @Delete
    suspend fun deleteSchedule(dayOfWeek: DayOfWeek)
}