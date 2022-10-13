package com.github.adamr22.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class StopWatchViewModel : ViewModel() {

    private var job: Job = Job()

    private var timeInMillis = 0

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
        timeInMillis = 0
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(10L)
                timeInMillis += 10
                _time.value = timeInMillis.toString()
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

        runStopWatch()
    }

    fun formatTime(time: String): List<Long> {
        val hours = TimeUnit.MILLISECONDS.toHours(time.toLong())
        val minutes =
            TimeUnit.MILLISECONDS.toMinutes(time.toLong() - TimeUnit.HOURS.toMillis(hours))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(
            time.toLong() - (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes))
        )
        val milliseconds = TimeUnit.MILLISECONDS.toMillis(
            time.toLong() - (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(
                seconds
            ))
        )

        return listOf(hours, minutes, seconds, milliseconds)
    }
}