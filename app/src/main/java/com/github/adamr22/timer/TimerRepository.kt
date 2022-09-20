package com.github.adamr22.timer

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

    fun deleteTimer(timerInstanceIndex: Int) {
        timersList[timerInstanceIndex].timer?.cancel()
        timersList.removeAt(timerInstanceIndex)
    }

    fun changeTimerState(index: Int, state: TimerViewModel.TimerStates) {
        timersList[index].timerState = state
    }
}