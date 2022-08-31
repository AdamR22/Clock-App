package com.github.adamr22.common.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.github.adamr22.common.broadcastreceivers.AlertPrecursorReceiver
import java.util.*

class AlertPrecursorService : Service() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var calenderInstance: Calendar
    private val CALENDAR_INSTANCE = "TIME"

    private val TAG = "AlertPrecursorService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        calenderInstance = intent?.getParcelableExtra(CALENDAR_INSTANCE)!!

        Log.d(TAG, "onStartCommand: Calender Instance: $calenderInstance")

        val mIntent = Intent(this, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this,
            1, mIntent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calenderInstance.timeInMillis, pendingIntent)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        val mIntent = Intent(this, AlertPrecursorReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this,
            1, mIntent, 0)

        alarmManager.cancel(pendingIntent)

        super.onDestroy()
    }
}