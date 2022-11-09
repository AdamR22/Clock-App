package com.github.adamr22.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Transaction
    @Query("SELECT * FROM alarm")
    fun getAllItems(): Flow<List<AlarmAndDay>?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :bedtime")
    fun getBedtimeItem(bedtime: String): Flow<AlarmAndDay?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :wakeup")
    fun getWakeUpItem(wakeup: String): Flow<AlarmAndDay?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE alarm_id = :id")
    fun getSpecificItem(id: Int): Flow<AlarmAndDay>

    @Query("UPDATE alarm SET is_scheduled = :value WHERE alarm_id = :id")
    suspend fun updateSchedule(value: Boolean, id: Int)

    @Delete
    suspend fun deleteItem(alarm: Alarm)

    @Update
    suspend fun updateItem(alarm: Alarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(alarm: Alarm)
}