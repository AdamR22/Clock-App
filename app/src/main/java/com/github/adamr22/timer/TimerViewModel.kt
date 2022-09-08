package com.github.adamr22.timer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {
    private var _timers = MutableStateFlow<MutableList<TimerModel>>(mutableListOf())
    private var _timer = MutableStateFlow("")

    var timer: StateFlow<String> = _timer

    fun setTime(input: String) {
        if (_timer.value.isNotEmpty() && input == "X") _timer.value.dropLast(1)
        if (_timer.value.length < 6 && input != "X") _timer.value += input
    }

}