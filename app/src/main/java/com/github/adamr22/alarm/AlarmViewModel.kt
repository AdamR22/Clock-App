package com.github.adamr22.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel() {
    private lateinit var alarmItems: MutableLiveData<ArrayList<AlarmItemModel>>

    init {
        alarmItems.value = ArrayList()
    }

    fun getAlarmItems(): LiveData<ArrayList<AlarmItemModel>> {
        return alarmItems
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        alarmItems.value?.add(alarm)
    }

    fun addLabel(label: String, index: Int) {
        val alarmItem = alarmItems.value?.get(index)
        alarmItem?.label = label
    }
}