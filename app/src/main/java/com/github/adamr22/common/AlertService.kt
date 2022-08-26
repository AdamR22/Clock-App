package com.github.adamr22.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.adamr22.R
import kotlin.math.roundToInt

class AlertService : JobService() {

    val ALARM_NOTIFICATION_CHANNEL_ID = "0"
    val ALARM_NOTIFICATION_CHANNEL_NAME = "Alarm Alerts"

    override fun onStartJob(params: JobParameters?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                ALARM_NOTIFICATION_CHANNEL_ID,
                ALARM_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).run {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(this)
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_black)
            .setContentTitle("Scheduled Alarm")
            .setContentText("Your scheduled alarm is 15 minutes away")
            .build()

        with(NotificationManagerCompat.from(applicationContext)) {
            this.notify(Math.random().roundToInt(), notification)
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

}
