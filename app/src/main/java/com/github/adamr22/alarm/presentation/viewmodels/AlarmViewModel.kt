package com.github.adamr22.alarm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.data.repositories.AlarmFakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlarmViewModel : ViewModel() {
    private val alarmRepository: AlarmFakeRepository = AlarmFakeRepository()
    private var _alarmItems: MutableStateFlow<AlarmUIState> =
        MutableStateFlow(AlarmUIState.Empty)
    val alarmItems: StateFlow<AlarmUIState> = _alarmItems

    private val TAG = "AlarmViewModel"

    sealed class AlarmUIState {
        object Empty : AlarmUIState()
        data class AlarmItems(val alarmItems: List<AlarmItemModel>) : AlarmUIState()
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        alarmRepository.addAlarmItem(alarm)
        _alarmItems.value = AlarmUIState.AlarmItems(alarmRepository.getAlarmList())
    }

    fun addLabel(label: String, index: Int) {
        alarmRepository.addAlarmLabel(label, index)
        _alarmItems.value = AlarmUIState.AlarmItems(alarmRepository.getAlarmList())
    }
}