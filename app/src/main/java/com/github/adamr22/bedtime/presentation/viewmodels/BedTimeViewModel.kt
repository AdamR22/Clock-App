package com.github.adamr22.bedtime.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.adamr22.bedtime.data.BedtimeRepository
import com.github.adamr22.data.database.ClockAppDB
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.data.entities.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BedTimeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ClockAppDB.getInstance(application.applicationContext)

    private val alarmDao = db.alarmDao()
    private val scheduleDao = db.scheduleDao()

    private val repository = BedtimeRepository(alarmDao, scheduleDao)

    fun getBedtime(bedtimeLabel: String): Flow<AlarmAndDay?> =
        repository.getBedtime(bedtimeLabel)

    fun getWakeup(wakeupLabel: String): Flow<AlarmAndDay?> =
        repository.getWakeup(wakeupLabel)

    fun updateTime(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTime(alarm)
    }

    fun insertTime(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTime(alarm)
    }

    fun insertSchedule(schedule: Schedule) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertSchedule(schedule)
    }

    fun updateSchedule(schedule: Schedule) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSchedule(schedule)
    }
}