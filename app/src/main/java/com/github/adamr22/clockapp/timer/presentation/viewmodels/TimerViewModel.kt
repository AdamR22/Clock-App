package com.github.adamr22.clockapp.timer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.github.adamr22.clockapp.timer.data.models.TimerModel
import com.github.adamr22.clockapp.timer.data.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {
    private val UNIT = 60 // to determine seconds, minutes or hours depending on input string value
    var setTime = "" // prevents data loss as a result of screen rotation

    private val timerRepository = TimerRepository()
    private var _timers = MutableStateFlow<TimerFragmentUIState>(TimerFragmentUIState.Empty)
    private var _timerLabelState = MutableStateFlow<TimerLabelState>(TimerLabelState.Unchanged)

    val timers: StateFlow<TimerFragmentUIState> = _timers
    val timerLabelState: StateFlow<TimerLabelState> = _timerLabelState

    sealed class TimerFragmentUIState {
        data class Timers(val timerInstances: List<TimerModel>) : TimerFragmentUIState()
        object Empty : TimerFragmentUIState()
    }

    sealed class TimerLabelState {
        object Changed : TimerLabelState()
        object Unchanged : TimerLabelState()
    }

    enum class TimerStates {
        PAUSED, RUNNING, FINISHED
    }

    fun addTimer(input: String) {
        val c = Calendar.getInstance().apply {
            this.set(Calendar.HOUR_OF_DAY, 0)
            this.set(Calendar.MINUTE, 0)
            this.set(Calendar.SECOND, 0)

            when (input.length) {
                1 -> this.set(Calendar.SECOND, input.toInt())
                2 -> {
                    if (input.toInt() >= 60) {
                        val minutes = input.toInt() / UNIT
                        val seconds = input.toInt() % UNIT

                        this.set(Calendar.MINUTE, minutes)
                        this.set(Calendar.SECOND, seconds)
                    }

                    if (input.toInt() < 60) this.set(Calendar.SECOND, input.toInt())

                }

                3 -> {
                    val minuteVal = input.slice(0..0)
                    val secondsVal = input.slice(1..2)

                    this.set(Calendar.MINUTE, minuteVal.toInt())

                    if (secondsVal.toInt() >= UNIT) {
                        val minutes = secondsVal.toInt() / UNIT
                        val seconds = secondsVal.toInt() % UNIT

                        this.add(Calendar.MINUTE, minutes)
                        this.set(Calendar.SECOND, seconds)
                    } else {
                        this.set(Calendar.SECOND, secondsVal.toInt())
                    }
                }

                4 -> {
                    val minuteVal = input.slice(0..1)
                    val secondsVal = input.slice(2..3)


                    if (minuteVal.toInt() >= UNIT) {
                        val hours = minuteVal.toInt() / UNIT
                        val minutes = minuteVal.toInt() % UNIT

                        this.set(Calendar.HOUR_OF_DAY, hours)
                        this.set(Calendar.MINUTE, minutes)

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }

                    if (minuteVal.toInt() < UNIT) {
                        this.set(Calendar.MINUTE, minuteVal.toInt())

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }
                }

                5 -> {
                    val hourVal = input.slice(0..0)
                    val minuteVal = input.slice(1..2)
                    val secondsVal = input.slice(3..4)

                    this.set(Calendar.HOUR_OF_DAY, hourVal.toInt())

                    if (minuteVal.toInt() >= UNIT) {
                        val hours = minuteVal.toInt() / UNIT
                        val minutes = minuteVal.toInt() % UNIT

                        this.add(Calendar.HOUR_OF_DAY, hours)
                        this.set(Calendar.MINUTE, minutes)

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }

                    if (minuteVal.toInt() < UNIT) {
                        this.set(Calendar.MINUTE, minuteVal.toInt())

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }

                }

                6 -> {
                    val hourVal = input.slice(0..1)
                    val minuteVal = input.slice(2..3)
                    val secondsVal = input.slice(4..5)

                    this.set(Calendar.HOUR_OF_DAY, hourVal.toInt())

                    if (minuteVal.toInt() >= UNIT) {
                        val hours = minuteVal.toInt() / UNIT
                        val minutes = minuteVal.toInt() % UNIT

                        this.add(Calendar.HOUR_OF_DAY, hours)
                        this.set(Calendar.MINUTE, minutes)

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }

                    if (minuteVal.toInt() < UNIT) {
                        this.set(Calendar.MINUTE, minuteVal.toInt())

                        if (secondsVal.toInt() >= UNIT) {
                            val min = secondsVal.toInt() / UNIT
                            val seconds = secondsVal.toInt() % UNIT

                            this.add(Calendar.MINUTE, min)
                            this.set(Calendar.SECOND, seconds)
                        }

                        if (secondsVal.toInt() < UNIT)
                            this.set(Calendar.SECOND, secondsVal.toInt())
                    }
                }
            }
        }

        val timerModel = TimerModel(setTime = c, timerFinished = false)
        timerRepository.addTimer(timerModel)
        _timers.value = TimerFragmentUIState.Timers(timerRepository.getTimersList())
    }

    fun addLabel(index: Int, label: String) {
        _timerLabelState.value = TimerLabelState.Unchanged
        timerRepository.addLabel(index, label)
        _timers.value = TimerFragmentUIState.Timers(timerRepository.getTimersList())
        _timerLabelState.value = TimerLabelState.Changed
    }

    fun deleteTimer(position: Int) {
        timerRepository.deleteTimer(position)
        val timersList = timerRepository.getTimersList()
        _timers.value =
            if (timersList.isEmpty()) TimerFragmentUIState.Empty else TimerFragmentUIState.Timers(
                timersList
            )
    }

    fun convertTimeToMilliseconds(setTimeInstance: Calendar): Long {
        val hoursInMilli =
            TimeUnit.HOURS.toMillis(setTimeInstance.get(Calendar.HOUR_OF_DAY).toLong())
        val minutesInMilli =
            TimeUnit.MINUTES.toMillis(setTimeInstance.get(Calendar.MINUTE).toLong())
        val secondsInMilli =
            TimeUnit.SECONDS.toMillis(setTimeInstance.get(Calendar.SECOND).toLong())

        return (hoursInMilli + minutesInMilli + secondsInMilli)
    }

    fun convertMillisecondsToHoursMinutesAndSeconds(milliseconds: Long): Triple<Long, Long, Long> {
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds - TimeUnit.HOURS.toMillis(hours))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(
            milliseconds - (TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes))
        )

        return Triple(hours, minutes, seconds)
    }

}