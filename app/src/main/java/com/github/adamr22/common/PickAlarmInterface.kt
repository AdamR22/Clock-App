package com.github.adamr22.common

import android.net.Uri

interface PickAlarmInterface {
    fun selectAlarmTone()
    fun returnSelectedTone(): Pair<Uri, String>?
}