package com.github.adamr22.clockapp.utils

import android.net.Uri

interface PickAlarmInterface {
    fun selectAlarmTone()
    fun returnSelectedTone(): Pair<Uri, String>?
}