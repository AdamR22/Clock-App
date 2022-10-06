package com.github.adamr22.timer.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.github.adamr22.timer.data.models.TimerModel
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel

class TimerFragInBackgroundService : Service() {

    private val TAG = "TimerService"

    private val TIMERS_LIST = "timers list"
    private val sharedPreferenceTag = "RunTimerFragVal"

    private val sharedPref by lazy {
        applicationContext.getSharedPreferences(sharedPreferenceTag, MODE_PRIVATE)
    }

    private lateinit var timersList: ArrayList<TimerModel>

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onStartCommand: Timer Service started")
        val bundle = intent?.extras

        @Suppress("UNCHECKED_CAST")
        timersList =
            bundle?.getParcelableArrayList(TIMERS_LIST)!!

        runTimers(timersList)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        cancelTimers(timersList)
        super.onDestroy()
    }

    private fun runTimers(timersList: ArrayList<TimerModel>) {
        timersList.forEach { timerModel ->
            runTimer(timerModel)
        }
    }

    private fun runTimer(timerModel: TimerModel) {
        val remainingTimeTag = "RunTimerVal: ${timerModel.timerId}"
        val timerStateTag = "RunTimerState: ${timerModel.timerId}"

        val timeRemaining = sharedPref.getLong(remainingTimeTag, -1L)

        val timerState = if (sharedPref.getInt(
                timerStateTag,
                -1
            ) < 0
        ) TimerViewModel.TimerStates.RUNNING else TimerViewModel.TimerStates.values()[sharedPref.getInt(
            timerStateTag,
            -1
        )]

        if (timerState == TimerViewModel.TimerStates.RUNNING) {
            timerModel.timer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(timeUntilFinished: Long) {
                    Log.d(TAG, "onTick: Time Until Finished: ${timeUntilFinished / 1000} seconds")
                    timerModel.timeRemaining = timeUntilFinished
                }

                override fun onFinish() {
                    timerModel.timer?.cancel()
                    timerModel.timerState = TimerViewModel.TimerStates.FINISHED
                    // Publish notification to either delete timer or restart it
                    Toast.makeText(applicationContext, "Timer Finished", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
    }

    private fun cancelTimers(timersList: ArrayList<TimerModel>) {
        timersList.forEach { timerModel ->
            cancelTimer(timerModel)
        }
    }

    private fun cancelTimer(timerModel: TimerModel) {
        val remainingTimeTag = "RunTimerVal: ${timerModel.timerId}"
        val timerStateTag = "RunTimerState: ${timerModel.timerId}"

        timerModel.timer?.cancel()
        timersList.remove(timerModel)

        sharedPref.edit()
            .putLong(remainingTimeTag, timerModel.timeRemaining)
            .putInt(timerStateTag, timerModel.timerState.ordinal)
            .apply()
    }
}