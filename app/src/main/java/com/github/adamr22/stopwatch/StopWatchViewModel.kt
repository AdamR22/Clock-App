package com.github.adamr22.stopwatch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopWatchViewModel : ViewModel() {
    val initialOrdinalValue = 0
    val pauseOrdinalValue = 1
    val playOrdinalValue = 2

    enum class StopWatchStates {
        INITIAL,
        PAUSED,
        RUNNING,
    }

    private var _stopwatchState = MutableStateFlow(StopWatchStates.INITIAL)
    val stopwatchState: StateFlow<StopWatchStates> = _stopwatchState

    fun changeState(ordinalValue: Int) {
        _stopwatchState.value = StopWatchStates.values()[ordinalValue]
    }
}