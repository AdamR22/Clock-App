package com.github.adamr22.common

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import kotlin.math.roundToInt

class AlertPrecursorReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val componentName = ComponentName(it, AlertService::class.java)
            val job = JobInfo.Builder(Math.random().roundToInt(), componentName)
                .setPersisted(true)
                .build()

            val jobScheduler = it.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(job)
        }
    }
}