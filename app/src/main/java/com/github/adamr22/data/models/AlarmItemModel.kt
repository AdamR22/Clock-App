package com.github.adamr22.data.models

import android.net.Uri

data class AlarmItemModel(
    val id: Int,
    var label: String? = null,
    var hour: Int,
    var minute: Int,
    val schedule: ArrayList<String> = ArrayList(),
    var ringtoneTitle: String? = null,
    var ringtoneUri: Uri? = null,
    var isScheduled: Boolean = true,
    var reminder: Int = 15,
    var isVibrate: Boolean = true,
    var isSunriseMode: Boolean = true
)
