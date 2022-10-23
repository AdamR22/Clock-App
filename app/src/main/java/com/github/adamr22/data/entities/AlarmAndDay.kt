package com.github.adamr22.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlarmAndDay(
    @Embedded val alarm: Alarm,

    @Relation(
        parentColumn = "alarm_id",
        entityColumn = "schedule_owner_id"
    )

    val schedule: List<Schedule>
)
