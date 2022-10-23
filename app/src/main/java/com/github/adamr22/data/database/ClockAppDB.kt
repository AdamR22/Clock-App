package com.github.adamr22.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.adamr22.data.DAO.AlarmDao
import com.github.adamr22.data.DAO.ScheduleDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.Schedule

@Database(entities = [Alarm::class, Schedule::class], version = 1)
abstract class ClockAppDB : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ClockAppDB? = null

        fun getInstance(context: Context): ClockAppDB {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    ClockAppDB::class.java,
                    "clock_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}