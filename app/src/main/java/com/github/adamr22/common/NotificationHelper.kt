package com.github.adamr22.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {
    fun createNotificationChannel(channelID: String, channelName: String, context: Context) {
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
    }
}