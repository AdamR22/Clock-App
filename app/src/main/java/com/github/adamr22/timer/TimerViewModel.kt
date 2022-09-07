package com.github.adamr22.timer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TimerViewModel : ViewModel() {
    private var _timers = MutableStateFlow<MutableList<TimerModel>>(mutableListOf())

}