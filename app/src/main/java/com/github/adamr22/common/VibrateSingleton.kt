package com.github.adamr22.common

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibrateSingleton {
    private lateinit var vibrator: Vibrator
    private val VIBRATE: Int = 1000
    private val SLEEP: Int = 1000
    private val START: Int = 0
    private val DELAY: Int = 0

    fun vibrateDevice(context: Context, vibrate: Boolean) {
        val mVibrationPattern = longArrayOf(DELAY.toLong(), VIBRATE.toLong(), SLEEP.toLong())

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(mVibrationPattern, START))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(mVibrationPattern, START)
            }
        } else {
            vibrator.cancel()
        }

    }
}