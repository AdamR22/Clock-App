package com.github.adamr22.alarm

data class AlarmItemModel(
    private val label: String?,
    private val time: String,
    private val schedule: List<String>?
)
