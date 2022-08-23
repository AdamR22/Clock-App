package com.github.adamr22.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlertPrecursorReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.BOOT_COMPLETED") {
                TODO("Not yet implemented")
            }
        }
    }

    private fun createAlarmNotification() {

    }
}