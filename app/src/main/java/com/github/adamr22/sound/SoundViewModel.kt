package com.github.adamr22.sound

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SoundViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var ringtoneList: Map<String, String>

    init {
       getRingtones()
    }

    private fun getRingtones() {
        viewModelScope.launch {
            ringtoneList = RingtoneSingleton.getRingtones(getApplication<Application>().applicationContext)
        }
    }

    fun getRingtoneMap(): Map<String, String> {
        return ringtoneList
    }
}