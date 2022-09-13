package com.github.adamr22.timer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {
    private var _timers = MutableStateFlow<MutableList<TimerModel>>(mutableListOf())
    private var _timer = MutableStateFlow("")

    var timer: StateFlow<String> = _timer

    fun setTimer(input: String) {
        if (input == "X" && _timer.value.isNotEmpty()) {
            _timer.value = _timer.value.dropLast(1)
        }

        if (_timer.value.length < 6 && input != "X") {
            _timer.value += input
        }
    }

    fun addTimer(input: String) {
        // TODO: set timer function
    }

}