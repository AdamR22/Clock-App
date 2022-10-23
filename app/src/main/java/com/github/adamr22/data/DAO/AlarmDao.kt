package com.github.adamr22.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Transaction
    @Query("SELECT * FROM alarm")
    fun getAllItems(): Flow<List<AlarmAndSchedule>?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :bedtime")
    fun getBedtimeItem(bedtime: String): Flow<AlarmAndSchedule?>

    @Transaction
    @Query("SELECT * FROM alarm WHERE label = :wakeup")
    fun getWakeUpItem(wakeup: String): Flow<AlarmAndSchedule?>

    @Delete
    suspend fun deleteItem(alarm: Alarm)

    @Update
    suspend fun updateItem(alarm: Alarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(alarm: Alarm)
}