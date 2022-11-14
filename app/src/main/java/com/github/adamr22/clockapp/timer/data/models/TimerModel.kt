package com.github.adamr22.clockapp.timer.data.models

import android.os.CountDownTimer
import com.github.adamr22.clockapp.timer.presentation.viewmodels.TimerViewModel
import java.util.*

data class TimerModel(
    var setTime: Calendar,
    var label: String? = null,
    var timerState: TimerViewModel.TimerStates = TimerViewModel.TimerStates.RUNNING,
    var timer: CountDownTimer? = null,
    val timerId: Int = Math.random().toInt(),
    var timeRemaining: Long = TimerViewModel().convertTimeToMilliseconds(setTime),
    var timerFinished: Boolean = false
)
