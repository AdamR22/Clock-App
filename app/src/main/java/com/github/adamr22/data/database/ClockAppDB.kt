package com.github.adamr22.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.adamr22.data.Converter
import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.dao.DayOfWeekDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.DayOfWeek

@Database(entities = [Alarm::class, DayOfWeek::class], version = 1)
@TypeConverters(Converter::class)
abstract class ClockAppDB : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun dayOfWeekDao(): DayOfWeekDao

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