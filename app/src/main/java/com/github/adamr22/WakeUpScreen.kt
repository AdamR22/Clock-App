package com.github.adamr22

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.bedtime.presentation.viewmodels.BedTimeViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.utils.VibrateSingleton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

class WakeUpScreen : AppCompatActivity() {

    private val ALARM_ID_TAG = "alarm_id"

    private val viewModel by lazy {
        ViewModelProvider(this)[BedTimeViewModel::class.java]
    }

    private val animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.blink)
    }

    private var alarmId: Int? = null

    private lateinit var data: Alarm
    private var ringtone = Uri.parse("")

    private lateinit var btnDismissAlarm: FloatingActionButton
    private lateinit var tvTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wake_up_screen)

        alarmId = intent.getIntExtra(ALARM_ID_TAG, 0)
        btnDismissAlarm = findViewById(R.id.btn_dismiss_alarm)
        tvTime = findViewById(R.id.tv_time)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
    }

    override fun onResume() {

        alarmId?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.getItem(it).collectLatest {
                    data = it
                    ringtone = it.uri!!
                    dealWithAlarm(it)
                }
            }
        }

        btnDismissAlarm.setOnClickListener {
            cancelAlarm()
        }

        super.onResume()
    }

    private fun dealWithAlarm(data: Alarm) {
        val hour = "%02d".format(data.hour)
        val minute = "%02d".format(data.minute)

        tvTime.text = String.format(resources.getString(R.string.default_time_2, hour, minute))
        tvTime.startAnimation(animation)

        VibrateSingleton.vibrateDevice(applicationContext, true)
        ringtonePlaying(true)
    }

    private fun cancelAlarm() {
        VibrateSingleton.vibrateDevice(applicationContext, false)
        ringtonePlaying(false)
        data.let {
            if (!it.everyDay) {
                tvTime.clearAnimation()
                viewModel.updateSchedule(false, alarmId!!)
                cancelPendingIntent(alarmId!!)
            }

            if (it.everyDay) {
                tvTime.clearAnimation()
                finish()
            }
        }
    }

    override fun onDestroy() {
        RingtoneManager.getRingtone(applicationContext, data.uri!!).stop()
        VibrateSingleton.vibrateDevice(applicationContext, false)
        super.onDestroy()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelPendingIntent(alarmId: Int) {
        val pendingIntentId = (alarmId + 1) * 1000

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, WakeUpScreen::class.java)

        val pendingIntent = PendingIntent.getActivity(this, pendingIntentId, alarmIntent, 0)

        alarmManager.cancel(pendingIntent)

        finish()
    }

    private fun ringtonePlaying(value: Boolean) {
        if (value) {
            RingtoneManager.getRingtone(applicationContext, data.uri!!).play()
        } else {
            RingtoneManager.getRingtone(applicationContext, data.uri!!).stop()
        }
    }
}