package com.github.adamr22.clockapp.data.dao

import androidx.room.*
import com.github.adamr22.clockapp.data.entities.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Transaction
    @Query("SELECT * FROM alarm")
    fun getAllItems(): Flow<List<Alarm>?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :bedtime")
    fun getBedtimeItem(bedtime: String): Flow<Alarm?>

    @Query("UPDATE alarm SET label = :label WHERE alarm_id = :id")
    suspend fun updateAlarmLabel(label: String, id: Int)

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :wakeup")
    fun getWakeUpItem(wakeup: String): Flow<Alarm?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE alarm_id = :id")
    fun getSpecificItem(id: Int): Flow<Alarm>

    @Query("UPDATE alarm SET is_scheduled = :value WHERE alarm_id = :id")
    suspend fun updateSchedule(value: Boolean, id: Int)

    @Query("UPDATE alarm SET everyDay = :value WHERE alarm_id = :id")
    suspend fun updateDaily(value: Boolean, id: Int)

    @Query("UPDATE alarm SET expandedItem = :value WHERE alarm_id = :id")
    suspend fun updateExpandedItem(value: Boolean, id: Int)

    @Delete
    suspend fun deleteItem(alarm: Alarm)

    @Update
    suspend fun updateItem(alarm: Alarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(alarm: Alarm)
}