package com.github.adamr22.data.DAO

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.github.adamr22.data.entities.Schedule

interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
}