package com.github.adamr22.common

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.github.adamr22.common.broadcastreceivers.AlertPrecursorReceiver
import java.util.*

object AlarmHelper {
    private lateinit var alarmManager: AlarmManager
    private val ALARM_REQUEST_CODE = 1

    @SuppressLint("UnspecifiedImmutableFlag")
    fun createAlarm(c: Calendar, context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}