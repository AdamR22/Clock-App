package com.github.adamr22.bedtime.data

import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.dao.DayOfWeekDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.data.entities.DayOfWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class BedtimeRepository(
    private val alarmDao: AlarmDao,
    private val dayOfWeekDao: DayOfWeekDao
) {

    fun getBedtime(bedtime: String): Flow<AlarmAndDay?> = alarmDao.getBedtimeItem(bedtime).distinctUntilChanged()

    fun getWakeup(wakeup: String): Flow<AlarmAndDay?> = alarmDao.getWakeUpItem(wakeup).distinctUntilChanged()

    suspend fun updateTime(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun insertTime(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun insertSchedule(day: String, alarmId: Int) = dayOfWeekDao.insertSchedule(day, alarmId)

    suspend fun updateSchedule(day: String?, alarmId: Int) = dayOfWeekDao.updateSchedule(day, alarmId)
}