package com.github.adamr22.alarm.data.models

import android.net.Uri

data class AlarmItemModel(
    var label: String = "",
    var time: String,
    val schedule: ArrayList<String> = ArrayList(),
    var ringtoneTitle: String = "",
    var ringtoneUri: Uri = Uri.parse("")
) {
    constructor(time: String, ringtoneTitle: String, ringtoneUri: Uri) : this(
        "",
        time,
        ArrayList(),
        ringtoneTitle,
        ringtoneUri
    )

    override fun toString(): String {
        return """
            Label: $label,
            Time: $time,
            Schedule: $schedule,
            ringtoneTitle: $ringtoneTitle,
            ringtoneUri: $ringtoneUri
        """.trimIndent()
    }
}
