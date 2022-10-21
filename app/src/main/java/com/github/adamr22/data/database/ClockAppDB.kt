package com.github.adamr22.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.adamr22.data.schemas.AlarmContract
import com.github.adamr22.data.schemas.ScheduleContract

class ClockAppDB(
    context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ClockApp.db"

        fun getInstance(context: Context) = ClockAppDB(context)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            it.setForeignKeyConstraintsEnabled(true)
            it.execSQL(AlarmContract.SQL_CREATE_ALARM_ENTRIES)
            it.execSQL(ScheduleContract.SQL_CREATE_SCHEDULE_ENTRIES)
            onCreate(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            it.setForeignKeyConstraintsEnabled(false)
            it.execSQL(AlarmContract.SQL_DELETE_ALARM_ENTRIES)
            it.execSQL(ScheduleContract.SQL_DELETE_SCHEDULE_ENTRIES)
            onCreate(it)
        }
    }
}