package com.github.adamr22.bedtime.data

import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.entities.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class BedtimeRepository(
    private val alarmDao: AlarmDao
) {

    fun getBedtime(bedtime: String): Flow<Alarm?> = alarmDao.getBedtimeItem(bedtime).distinctUntilChanged()

    fun getWakeup(wakeup: String): Flow<Alarm?> = alarmDao.getWakeUpItem(wakeup).distinctUntilChanged()

    suspend fun updateTime(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun insertTime(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun updateDaily(value: Boolean, id: Int) = alarmDao.updateDaily(value, id)

    suspend fun updateExpandedItem(value: Boolean, id: Int) = alarmDao.updateExpandedItem(value, id)
}