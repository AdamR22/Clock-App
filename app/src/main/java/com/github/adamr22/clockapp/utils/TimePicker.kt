package com.github.adamr22.clockapp.utils

import com.github.adamr22.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

object TimePicker {
    fun buildPicker(titleText: String): MaterialTimePicker {
        return MaterialTimePicker.Builder().setTheme(R.style.TimePickerLight)
            .setTimeFormat(TimeFormat.CLOCK_24H).setTitleText(titleText).build()
    }
}