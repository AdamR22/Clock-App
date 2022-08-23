package com.github.adamr22.common

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

object AlarmHelper {
    private lateinit var alarmManager: AlarmManager
    private val ALARM_REQUEST_CODE = 1

    @SuppressLint("UnspecifiedImmutableFlag")
    fun createAlarm(c: Calendar, context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val intent2 = Intent(context, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)
        val pendingIntentTwo = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent2, 0)

        val alarmPrecursorTime: Calendar = c
        alarmPrecursorTime.add(Calendar.MINUTE, -15)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmPrecursorTime.timeInMillis, pendingIntentTwo)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val intent2 = Intent(context, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)
        val pendingIntentTwo = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent2, 0)

        alarmManager.cancel(pendingIntentTwo)
        alarmManager.cancel(pendingIntent)
    }
}