package com.github.adamr22.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.github.adamr22.data.entities.DayOfWeek

@Dao
interface DayOfWeekDao {
    @Insert
    suspend fun insertSchedule(dayOfWeek: DayOfWeek)

    @Update
    suspend fun updateSchedule(dayOfWeek: DayOfWeek)

    @Delete
    suspend fun deleteSchedule(dayOfWeek: DayOfWeek)
}