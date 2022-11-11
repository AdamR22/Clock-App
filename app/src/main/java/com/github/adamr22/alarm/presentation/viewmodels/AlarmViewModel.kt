package com.github.adamr22.alarm.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.adamr22.alarm.data.repositories.AlarmRepository
import com.github.adamr22.data.database.ClockAppDB
import com.github.adamr22.data.entities.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ClockAppDB.getInstance(application.applicationContext)

    private val alarmDao = db.alarmDao()

    private val repository = AlarmRepository(alarmDao)

    fun getData() = repository.getAllItems()

    fun getAllDataApartFromBedtime() = repository.getAllApartFromBedtime()

    fun getItem(id: Int) = repository.getItem(id)

    fun updateSchedule(value: Boolean, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateSchedule(value, id) }

    fun updateAlarmLabel(label: String, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateAlarmLabel(label, id) }

    fun insertAlarm(alarm: Alarm) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertAlarm(alarm) }

    fun updateAlarm(alarm: Alarm) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateAlarm(alarm) }

    fun deleteAlarm(alarm: Alarm) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAlarm(alarm) }

    fun updateDaily(value: Boolean, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateDaily(value, id) }

    fun updateExpandedItem(value: Boolean, id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateExpandedItem(value, id) }
}