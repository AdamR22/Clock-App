package com.github.adamr22.timer.data.repository

import com.github.adamr22.timer.data.models.TimerModel

class TimerRepository {

    private val timersList = mutableListOf<TimerModel>()

    fun getTimersList(): List<TimerModel> {
        return timersList.toList()
    }

    fun addTimer(timerInstance: TimerModel) {
        timersList.add(timerInstance)
    }

    fun addLabel(index: Int, label: String) {
        timersList[index].label = label
    }

    fun deleteTimer(position: Int) {
        timersList.removeAt(position)
    }
}