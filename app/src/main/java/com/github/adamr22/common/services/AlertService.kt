package com.github.adamr22.common.services

import android.app.job.JobParameters
import android.app.job.JobService

class AlertService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        // TODO: Create service that starts alarm screen
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

}
