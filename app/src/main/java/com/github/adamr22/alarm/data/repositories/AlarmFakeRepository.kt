package com.github.adamr22.alarm.data.repositories

import com.github.adamr22.alarm.data.models.AlarmItemModel

class AlarmFakeRepository {
    private val alarmItemList: ArrayList<AlarmItemModel> = ArrayList()

    fun getAlarmList(): ArrayList<AlarmItemModel> {
        return alarmItemList
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        alarmItemList.add(alarm)
    }

    fun addAlarmLabel(label: String, index: Int) {
        alarmItemList[index].label = label
    }

    fun deleteAlarm(index: Int) {
        alarmItemList.removeAt(index)
    }
}