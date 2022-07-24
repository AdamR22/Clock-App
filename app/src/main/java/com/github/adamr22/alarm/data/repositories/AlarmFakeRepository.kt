package com.github.adamr22.alarm.data.repositories

import com.github.adamr22.alarm.data.models.AlarmItemModel

class AlarmFakeRepository {
    private var _alarmItemList: MutableList<AlarmItemModel> = mutableListOf()

    fun getAlarmList(): List<AlarmItemModel> {
        val newList = mutableListOf<AlarmItemModel>()
        _alarmItemList.forEach {
            newList.add(it)
        }

        return newList
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
}