package com.github.adamr22.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.BOOT_COMPLETED") {
                createAlarmNotification()
            }
        }
    }

    private fun createAlarmNotification() {

    }
}