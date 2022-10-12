package com.github.adamr22.stopwatch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopWatchViewModel : ViewModel() {

    private val TAG = "StopWatchViewModel"

    private var job: Job = Job()

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

    fun runStopWatch() {
        var timeInMillis = 0
        job = viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                delay(1000L)
                timeInMillis += 1000
                Log.d(TAG, "runStopwatch: time in mills: ${timeInMillis / 1000} seconds")
            }
        }

        job.start()
    }

    fun cancelStopWatch() {
        if (job.isActive) job.cancel()
    }

    fun resetStopWatch() {
        cancelStopWatch()
    }
}