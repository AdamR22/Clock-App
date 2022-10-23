package com.github.adamr22.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alarm_id")
    val id: Int? = null,

    @ColumnInfo(name = "label")
    var label: String?,

    @ColumnInfo(name = "ringtone_title")
    var title: String? = null,

    @ColumnInfo(name = "ringtone_uri")
    var uri: Uri? = null,

    @ColumnInfo(name = "is_scheduled")
    var isScheduled: Boolean = true,

    @ColumnInfo(name = "sunrise_mode_on")
    var sunriseMode: Boolean,

    @ColumnInfo(name = "vibrate_on")
    var vibrates: Boolean,

    var hour: Int,

    var minute: Int,

    var reminder: Int = 15,
)