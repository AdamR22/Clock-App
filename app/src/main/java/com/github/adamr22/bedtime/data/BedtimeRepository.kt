package com.github.adamr22.bedtime.data

import android.net.Uri
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.data.database.ClockAppDB
import com.github.adamr22.data.repository.ClockAppRepository

class BedtimeRepository(private val db: ClockAppDB) {

    private val databaseRepository = ClockAppRepository.getInstance(db)

    fun destroyDB() = databaseRepository.destroyDB()

    fun setTime(
        hour: Int,
        minute: Int,
        label: String,
        title: String,
        uri: Uri
    ) = databaseRepository.setTime(hour, minute, label, title, uri.toString())

    fun addSchedule(id: Int, schedule: Array<String>) = databaseRepository.addSchedule(id, schedule)

    fun removeDayFromSchedule(id: Int, day: String) = databaseRepository.removeDayFromSchedule(id, day)

    fun updateTime(hour: Int, minute: Int, id: Int) = databaseRepository.updateTime(hour, minute, id)

    fun updateAlarmTone(id: Int, title: String, uri: Uri) = databaseRepository.updateAlarmTone(id, title, uri.toString())

    fun getLabeledItem(label: String): AlarmItemModel = databaseRepository.getItem(label)

}