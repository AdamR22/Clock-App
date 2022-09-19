package com.github.adamr22.timer

import android.os.CountDownTimer
import java.util.*

data class TimerModel(
    var setTime: Calendar,
    var label: String? = null,
    var timerState: TimerViewModel.TimerStates = TimerViewModel.TimerStates.RUNNING,
    var timer: CountDownTimer? = null
)
