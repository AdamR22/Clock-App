package com.github.adamr22.alarm.data.repositories

import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.entities.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class AlarmRepository(private val alarmDao: AlarmDao) {
    fun getAllItems(): Flow<List<Alarm>?> = alarmDao.getAllItems().distinctUntilChanged()

    fun getAllApartFromBedtime(): Flow<List<Alarm>?> = alarmDao.getAllApartFromBedtime().distinctUntilChanged()

    suspend fun insertAlarm(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun updateAlarm(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun updateAlarmLabel(label: String, id: Int) = alarmDao.updateAlarmLabel(label, id)

    fun getItem(id: Int): Flow<Alarm> = alarmDao.getSpecificItem(id).distinctUntilChanged()

    suspend fun updateSchedule(value: Boolean, id: Int) = alarmDao.updateSchedule(value, id)

    suspend fun deleteAlarm(alarm: Alarm) = alarmDao.deleteItem(alarm)

    suspend fun updateDaily(value: Boolean, id: Int) = alarmDao.updateDaily(value, id)

    suspend fun updateExpandedItem(value: Boolean, id: Int) = alarmDao.updateExpandedItem(value, id)
}