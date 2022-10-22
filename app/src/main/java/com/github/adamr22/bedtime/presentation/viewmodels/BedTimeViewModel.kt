package com.github.adamr22.bedtime.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.bedtime.data.BedtimeRepository
import com.github.adamr22.data.database.ClockAppDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BedTimeViewModel : ViewModel() {
    private var repository: BedtimeRepository? = null

    fun setRepo(db: ClockAppDB) {
        repository = BedtimeRepository(db)
    }

    private var _bedtimeItem = MutableStateFlow<AlarmItemModel?>(null)
    private var _wakeUpItem = MutableStateFlow<AlarmItemModel?>(null)

    val bedtimeItem: StateFlow<AlarmItemModel?> = _bedtimeItem
    val wakeUpItem: StateFlow<AlarmItemModel?> = _wakeUpItem

    fun switchAlarm(id: Int, bit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository?.alarmSwitch(id, bit)
        }
    }

    private fun getLabeledItem(label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarmItem = repository?.getLabeledItem(label)

            withContext(Dispatchers.Main) {
                if (label == "BEDTIME") _bedtimeItem.value = alarmItem

                if (label == "WAKE UP") _wakeUpItem.value = alarmItem
            }
        }
    }

    fun setItem(hour: Int, minute: Int, label: String, title: String?, uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository?.setTime(hour, minute, label, title, uri)
        }

        getLabeledItem(label)
    }

    fun updateTime(label: String, id: Int, hour: Int, minute: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository?.updateTime(hour, minute, id)
        }
        getLabeledItem(label)
    }

    fun addSchedule(label: String, id: Int, schedule: Array<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository?.addSchedule(id, schedule)
        }

        getLabeledItem(label)
    }

    fun removeDayFromSchedule(label: String, id: Int, day: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repository?.removeDayFromSchedule(id, day)
        }

        getLabeledItem(label)
    }

    fun updateAlarmTone(label: String, id: Int, title: String, uri: Uri) {

        viewModelScope.launch(Dispatchers.IO) {
            repository?.updateAlarmTone(id, title, uri)
        }

        getLabeledItem(label)
    }

    fun updateReminder(label: String, id: Int, time: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            repository?.updateReminder(id, time)
        }

        getLabeledItem(label)
    }
}