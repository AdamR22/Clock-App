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

    private val lapTimeStamps = mutableListOf<String>()

    private var _lapTimes = MutableStateFlow(listOf<String>())
    val lapTimes: StateFlow<List<String>> = _lapTimes

    private var _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private var _stopwatchState = MutableStateFlow(StopWatchStates.INITIAL)
    val stopwatchState: StateFlow<StopWatchStates> = _stopwatchState

    fun changeState(ordinalValue: Int) {
        _stopwatchState.value = StopWatchStates.values()[ordinalValue]
    }

    fun runStopWatch() {
        var timeInMillis = 0
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000L)
                timeInMillis += 1000
                _time.value = timeInMillis.toString()
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

    fun lapTime() {
        cancelStopWatch()

        lapTimeStamps.add(_time.value)
        _lapTimes.value = lapTimeStamps.toList()

        _time.value = ""

        Log.d(TAG, "lapTime: ${_lapTimes.value.size}")
        runStopWatch()
    }
}