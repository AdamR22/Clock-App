package com.github.adamr22.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.adamr22.R
import kotlin.math.roundToInt

object NotificationHelper {
    fun createNotification(channelID: String, channelName: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).run {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(this)
            }
        }

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_notifications_active_black)
            .setContentTitle("Scheduled Alarm")
            .setContentText("Fifteen minutes to scheduled alarm")
            .build()

        with(NotificationManagerCompat.from(context)) {
            this.notify(Math.random().roundToInt(), notification)
        }
    }

}