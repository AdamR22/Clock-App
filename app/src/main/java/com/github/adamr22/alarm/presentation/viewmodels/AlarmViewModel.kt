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
    private val dayOfWeekDao = db.dayOfWeekDao()

    private val repository = AlarmRepository(alarmDao, dayOfWeekDao)

    fun getData() = repository.getAllItems()

    fun updateAlarm(alarm: Alarm) =
        viewModelScope.launch(Dispatchers.IO) { repository.updateAlarm(alarm) }
}