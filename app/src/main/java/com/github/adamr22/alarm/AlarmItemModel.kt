package com.github.adamr22.alarm

data class AlarmItemModel(
    var label: String?,
    var time: String,
    val schedule: ArrayList<String> = ArrayList()
)
