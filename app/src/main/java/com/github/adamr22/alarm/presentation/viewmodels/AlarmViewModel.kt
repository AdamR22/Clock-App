package com.github.adamr22.alarm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.data.repositories.AlarmFakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlarmViewModel : ViewModel() {
    private val alarmRepository: AlarmFakeRepository = AlarmFakeRepository()
    private var _alarmItems: MutableStateFlow<AlarmUIState> =
        MutableStateFlow(AlarmUIState.Empty)
    val alarmItems: StateFlow<AlarmUIState> = _alarmItems
    private var _labelChanged: MutableStateFlow<LabelChangedState> = MutableStateFlow(LabelChangedState.Unchanged)
    val labelChanged = _labelChanged

    sealed class AlarmUIState {
        object Empty : AlarmUIState()
        data class AlarmItems(val alarmItems: List<AlarmItemModel>) : AlarmUIState()
    }

    sealed class LabelChangedState {
        object Unchanged : LabelChangedState()
        object Changed : LabelChangedState()
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        alarmRepository.addAlarmItem(alarm)
        _alarmItems.value = AlarmUIState.AlarmItems(alarmRepository.getAlarmList())
    }

    fun addLabel(label: String, index: Int) {
        _labelChanged.value = LabelChangedState.Unchanged
        alarmRepository.addAlarmLabel(label, index)
        _alarmItems.value = AlarmUIState.AlarmItems(alarmRepository.getAlarmList())
        _labelChanged.value = LabelChangedState.Changed
    }

    fun deleteItem(index: Int) {
        alarmRepository.deleteAlarm(index)
        val listSize = alarmRepository.getAlarmList().size

        if (listSize == 0) {
            _alarmItems.value = AlarmUIState.Empty
        }
    }

    fun addDayToSchedule(index: Int, day: String) {
        alarmRepository.addDayOfWeekToSchedule(index, day)
    }

    fun removeDayOnSchedule(index: Int, day: String) {
        alarmRepository.removeDayOfWeekOnSchedule(index, day)
    }

    fun changeTime(index: Int, newTime: String) {
        alarmRepository.changeTime(index, newTime)
    }
}