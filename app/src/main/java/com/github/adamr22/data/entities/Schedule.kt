package com.github.adamr22.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val id: Int? = null,

    var day: String? = null,

    @ColumnInfo(name = "schedule_owner_id")
    val alarmId: Int,
)
