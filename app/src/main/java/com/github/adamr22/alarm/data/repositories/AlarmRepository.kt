package com.github.adamr22.alarm.data.repositories

import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.dao.DayOfWeekDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.data.entities.DayOfWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class AlarmRepository(private val alarmDao: AlarmDao, private val dayOfWeekDao: DayOfWeekDao) {
    fun getAllItems(): Flow<List<AlarmAndDay>?> = alarmDao.getAllItems().distinctUntilChanged()

    fun getAllApartFromBedtime(): Flow<List<AlarmAndDay>?> = alarmDao.getAllApartFromBedtime().distinctUntilChanged()

    suspend fun insertAlarm(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun updateAlarm(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun updateAlarmLabel(label: String, id: Int) = alarmDao.updateAlarmLabel(label, id)

    fun getItem(id: Int): Flow<AlarmAndDay> = alarmDao.getSpecificItem(id).distinctUntilChanged()

    suspend fun updateSchedule(value: Boolean, id: Int) = alarmDao.updateSchedule(value, id)

    suspend fun deleteAlarm(alarm: Alarm) = alarmDao.deleteItem(alarm)

    suspend fun insertSchedule(day: String, alarmId: Int) = dayOfWeekDao.insertSchedule(day, alarmId)

    suspend fun deleteDayFromSchedule(day: String, alarmId: Int) = dayOfWeekDao.deleteDayFromSchedule(day, alarmId)

    suspend fun deleteSchedule(dayOfWeek: DayOfWeek) = dayOfWeekDao.deleteSchedule(dayOfWeek)
}