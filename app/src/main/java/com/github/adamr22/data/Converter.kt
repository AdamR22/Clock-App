package com.github.adamr22.data

import android.net.Uri
import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun stringToUri(string: String?): Uri? {
        return if (string == null) null else Uri.parse(string)
    }
}