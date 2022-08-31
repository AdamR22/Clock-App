package com.github.adamr22.common.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            // TODO: Write code that starts an activity that wakes up phone
        }
    }
}