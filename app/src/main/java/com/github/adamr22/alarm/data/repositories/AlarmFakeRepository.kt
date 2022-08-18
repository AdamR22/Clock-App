package com.github.adamr22.alarm.data.repositories

import android.net.Uri
import com.github.adamr22.alarm.data.models.AlarmItemModel

class AlarmFakeRepository {
    private var _alarmItemList: MutableList<AlarmItemModel> = mutableListOf()

    fun getAlarmList(): List<AlarmItemModel> {
        return _alarmItemList.toList()
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        _alarmItemList.add(alarm)
    }

    fun addAlarmLabel(label: String, index: Int) {
        _alarmItemList[index].label = label
    }

    fun deleteAlarm(index: Int) {
        _alarmItemList.removeAt(index)
    }

    fun addDayOfWeekToSchedule(index: Int, day: String) {
        _alarmItemList[index].schedule.add(day)
    }

    fun removeDayOfWeekOnSchedule(index: Int, day: String) {
        _alarmItemList[index].schedule.remove(day)
    }

    fun changeTime(index: Int, newTime: String) {
        _alarmItemList[index].time = newTime
    }

    fun changeRingtone(index: Int, title: String, uri: Uri) {
        _alarmItemList[index].ringtoneTitle = title
        _alarmItemList[index].ringtoneUri = uri
    }
}