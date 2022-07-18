package com.github.adamr22.alarm.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.data.repositories.AlarmFakeRepository

class AlarmViewModel : ViewModel() {
    private var alarmItems: MutableLiveData<ArrayList<AlarmItemModel>>? = null
    private val alarmRepository: AlarmFakeRepository = AlarmFakeRepository()

    private val TAG = "AlarmViewModel"

    fun getAlarmItems(): MutableLiveData<ArrayList<AlarmItemModel>> {

        if (alarmItems == null) {
            alarmItems = MutableLiveData()
            alarmItems?.value = alarmRepository.getAlarmList()
        }

        return alarmItems!!
    }

    fun addAlarmItem(alarm: AlarmItemModel) {
        alarmRepository.addAlarmItem(alarm)
        Log.d(TAG, "addAlarmItem: Function Triggered")
        Log.d(TAG, "addAlarmItem: ${alarm.time}")
    }

    fun addLabel(label: String, index: Int) {
        alarmRepository.addAlarmLabel(label, index)
    }
}