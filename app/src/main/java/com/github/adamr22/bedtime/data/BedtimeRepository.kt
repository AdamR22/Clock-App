package com.github.adamr22.bedtime.data

import com.github.adamr22.data.dao.AlarmDao
import com.github.adamr22.data.dao.ScheduleDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.data.entities.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class BedtimeRepository(
    private val alarmDao: AlarmDao,
    private val scheduleDao: ScheduleDao
) {

    fun getBedtime(bedtime: String): Flow<AlarmAndDay?> = alarmDao.getBedtimeItem(bedtime).distinctUntilChanged()

    fun getWakeup(wakeup: String): Flow<AlarmAndDay?> = alarmDao.getWakeUpItem(wakeup).distinctUntilChanged()

    suspend fun updateTime(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun insertTime(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)

    suspend fun updateSchedule(schedule: Schedule) = scheduleDao.updateSchedule(schedule)
}