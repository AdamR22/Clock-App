package com.github.adamr22.bedtime

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BedTimeViewModel : ViewModel() {
    private var _bedTimeScheduled = MutableStateFlow(false)
    val bedTimeScheduled: StateFlow<Boolean> = _bedTimeScheduled

    private var _wakeUpTimeScheduled = MutableStateFlow(false)
    val wakeUpTimeScheduled: StateFlow<Boolean> = _wakeUpTimeScheduled

    private var _notificationReminderText = MutableStateFlow("")
    val notificationReminderText: StateFlow<String> = _notificationReminderText

    fun scheduleBedTime() {
        _bedTimeScheduled.value = !_bedTimeScheduled.value
    }

    fun scheduleWakeUpTime() {
        _wakeUpTimeScheduled.value = !_wakeUpTimeScheduled.value
    }

    fun notificationReminderText(text: String) {
        // place holder function for notification reminder text
        _notificationReminderText.value = text
    }

}