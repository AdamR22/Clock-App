package com.github.adamr22.alarm.data.models

import android.net.Uri

data class AlarmItemModel(
    val id: Int,
    var label: String = "",
    var hour: Int,
    var minute: Int,
    val schedule: ArrayList<String> = ArrayList(),
    var ringtoneTitle: String = "",
    var ringtoneUri: Uri = Uri.parse(""),
    var isScheduled: Boolean = true,
    var reminder: Int = 15
)
