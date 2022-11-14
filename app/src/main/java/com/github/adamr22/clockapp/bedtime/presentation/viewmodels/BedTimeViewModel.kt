package com.github.adamr22.clockapp.bedtime.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.adamr22.clockapp.bedtime.data.BedtimeRepository
import com.github.adamr22.clockapp.data.database.ClockAppDB
import com.github.adamr22.clockapp.data.entities.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BedTimeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ClockAppDB.getInstance(application.applicationContext)

    private val alarmDao = db.alarmDao()

    private val repository = BedtimeRepository(alarmDao)

    fun getBedtime(bedtimeLabel: String): Flow<Alarm?> =
        repository.getBedtime(bedtimeLabel)

    fun getWakeup(wakeupLabel: String): Flow<Alarm?> =
        repository.getWakeup(wakeupLabel)

    fun updateTime(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTime(alarm)
    }

    fun insertTime(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTime(alarm)
    }

    fun updateDaily(value: Boolean, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateDaily(value, id) }

    fun getData() = repository.getData()

    fun getItem(id: Int) = repository.getItem(id)

    fun updateSchedule(value: Boolean, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateSchedule(value, id) }
}