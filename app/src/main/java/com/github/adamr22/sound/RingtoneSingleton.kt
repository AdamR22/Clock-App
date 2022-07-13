package com.github.adamr22.sound

import android.content.Context
import android.database.Cursor
import android.media.RingtoneManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RingtoneSingleton {
    suspend fun getRingtones(context: Context): Map<String, String> {
        val manager = RingtoneManager(context)
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor: Cursor = manager.cursor
        val list = mutableMapOf<String, String>()
        withContext(Dispatchers.IO) {
            while (cursor.moveToNext()) {
                val ringtoneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val ringtoneUri =
                    cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(
                        RingtoneManager.ID_COLUMN_INDEX
                    )

                list[ringtoneTitle] = ringtoneUri
            }
        }

        return list
    }

    // TODO: Create functions that set desired ringtone, play selected ringtone and get default ringtone
}