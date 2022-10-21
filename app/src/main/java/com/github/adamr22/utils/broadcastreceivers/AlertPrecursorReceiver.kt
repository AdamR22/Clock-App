package com.github.adamr22.utils.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.adamr22.utils.NotificationHelper

class AlertPrecursorReceiver: BroadcastReceiver() {
    private val ALARM_CHANNEL_ID = "alarm channel id"
    private val ALARM_CHANNEL_NAME = "alarm channel name"

    private val TAG = "AlertPrecursorReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: Triggered")
        NotificationHelper.createNotificationChannel(ALARM_CHANNEL_ID, ALARM_CHANNEL_NAME, context!!)
    }
}