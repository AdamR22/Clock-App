package com.github.adamr22.bedtime.data

import com.github.adamr22.data.DAO.AlarmDao
import com.github.adamr22.data.DAO.ScheduleDao
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndSchedule
import com.github.adamr22.data.entities.Schedule
import kotlinx.coroutines.flow.Flow

class BedtimeRepository(private val alarmDao: AlarmDao, private val scheduleDao: ScheduleDao) {

    fun getBedtime(bedtime: String): Flow<AlarmAndSchedule?> = alarmDao.getBedtimeItem(bedtime)

    fun getWakeup(wakeup: String): Flow<AlarmAndSchedule?> = alarmDao.getWakeUpItem(wakeup)

    suspend fun updateTime(alarm: Alarm) = alarmDao.updateItem(alarm)

    suspend fun insertTime(alarm: Alarm) = alarmDao.insertItem(alarm)

    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)

    suspend fun updateSchedule(schedule: Schedule) = scheduleDao.updateSchedule(schedule)
}