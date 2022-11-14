package com.github.adamr22.clockapp.data.models

import android.net.Uri

data class AlarmItemModel(
    val id: Int,
    var label: String? = null,
    var hour: Int,
    var minute: Int,
    var ringtoneTitle: String? = null,
    var ringtoneUri: Uri? = null,
    var isScheduled: Boolean = true,
    var isVibrate: Boolean = true,
    var isSunriseMode: Boolean = true,
    var expandedItem: Boolean = false,
    var everyDay: Boolean = false
)
