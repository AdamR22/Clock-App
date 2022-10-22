package com.github.adamr22.data.repository

import android.content.ContentValues
import android.net.Uri
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.data.database.ClockAppDB
import com.github.adamr22.data.schemas.AlarmContract
import com.github.adamr22.data.schemas.ScheduleContract

class ClockAppRepository(private val db: ClockAppDB) {

    private val appDB = db.writableDatabase
    private val readAppDb = db.readableDatabase
    private val contentValues = ContentValues()
    private val ALARM_TABLE_NAME = AlarmContract.AlarmEntry.TABLE_NAME
    private val SCHEDULE_TABLE_NAME = ScheduleContract.ScheduleEntry.TABLE_NAME

    companion object {
        fun getInstance(db: ClockAppDB) = ClockAppRepository(db)
    }

    fun destroyDB() {
        db.close()
    }

    fun setTime(
        hour: Int,
        minute: Int,
        label: String?,
        ringtoneTitle: String?,
        ringtoneUri: String?,
    ) {

        val values = contentValues.apply {
            put(AlarmContract.AlarmEntry.COLUMN_NAME_HOUR, hour)
            put(AlarmContract.AlarmEntry.COLUMN_NAME_MINUTE, minute)
            put(AlarmContract.AlarmEntry.COLUMN_NAME_LABEL, label)
            put(AlarmContract.AlarmEntry.COLUMN_NAME_IS_SCHEDULED, 1)
            put(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_TITLE, ringtoneTitle)
            put(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_URI, ringtoneUri)
        }

        appDB.insert(ALARM_TABLE_NAME, null, values)
    }

    fun changeLabel(label: String, id: Int) {
        val values = contentValues.apply {
            put(AlarmContract.AlarmEntry.COLUMN_NAME_LABEL, label)
        }

        val selection = "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} LIKE ?"
        val selectionArgs = arrayOf(id.toString())

        appDB.update(ALARM_TABLE_NAME, values, selection, selectionArgs)
    }

    fun deleteItem(id: Int) {

        val scheduleWhereClause = "${ScheduleContract.ScheduleEntry.COLUMN_NAME_ALARM_ID} = ?"
        val alarmWhereClause = "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        appDB.delete(SCHEDULE_TABLE_NAME, scheduleWhereClause, selectionArgs)
        appDB.delete(ALARM_TABLE_NAME, alarmWhereClause, selectionArgs)
    }

    fun addSchedule(id: Int, schedule: Array<String>) {
        val values = contentValues.apply {
            this.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_ALARM_ID, id)
            for (day in schedule) {
                this.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_DAY, day)
            }
        }

        appDB.insert(ScheduleContract.ScheduleEntry.TABLE_NAME, null, values)
    }

    fun removeDayFromSchedule(id: Int, day: String) {
        val whereClause =
            "${ScheduleContract.ScheduleEntry.COLUMN_NAME_ALARM_ID} = ? AND ${ScheduleContract.ScheduleEntry.COLUMN_NAME_DAY} = ?"
        val selectionArgs = arrayOf(id.toString(), day)

        appDB.delete(SCHEDULE_TABLE_NAME, whereClause, selectionArgs)
    }

    fun updateTime(hour: Int, minute: Int, id: Int) {
        val values = contentValues.apply {
            this.put(AlarmContract.AlarmEntry.COLUMN_NAME_HOUR, hour)
            this.put(AlarmContract.AlarmEntry.COLUMN_NAME_MINUTE, minute)
        }

        val whereClause = "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} = ?"
        val whereArgs = arrayOf(id.toString())

        appDB.update(ALARM_TABLE_NAME, values, whereClause, whereArgs)
    }

    fun updateAlarmTone(id: Int, title: String, Uri: String) {
        val values = contentValues.apply {
            this.put(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_TITLE, title)
            this.put(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_URI, Uri)
        }

        val whereClause = "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} = ?"
        val whereArgs = arrayOf(id.toString())

        appDB.update(ALARM_TABLE_NAME, values, whereClause, whereArgs)
    }

    fun getItems(): List<AlarmItemModel> {
        val alarmItems = mutableListOf<AlarmItemModel>()

        val cursor = readAppDb.query(ALARM_TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_ID))
                val hour = getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_HOUR))
                val minute =
                    getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_MINUTE))
                val label =
                    getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_LABEL))
                val ringtoneTitle =
                    getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_TITLE))
                val ringtoneUri =
                    Uri.parse(getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_URI)))
                val isScheduled =
                    getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_IS_SCHEDULED)) != 0
                val reminder =
                    if (getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER)) != 60) getInt(
                        getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER)
                    ) else 1

                val schedule = getSchedule(itemId)

                val item = AlarmItemModel(
                    itemId,
                    label,
                    hour,
                    minute,
                    schedule,
                    ringtoneTitle,
                    ringtoneUri,
                    isScheduled,
                    reminder
                )

                alarmItems.add(item)
            }
        }

        cursor.close()

        return alarmItems.toList()
    }

    private fun getSchedule(id: Int): java.util.ArrayList<String> {
        val schedule = ArrayList<String>()
        val scheduleSelection = "${ScheduleContract.ScheduleEntry.COLUMN_NAME_ALARM_ID} = ?"
        val scheduleSelectionArgs = arrayOf(id.toString())

        val scheduleCursor = readAppDb.query(
            ScheduleContract.ScheduleEntry.TABLE_NAME,
            null,
            scheduleSelection,
            scheduleSelectionArgs,
            null,
            null,
            null
        )

        while (scheduleCursor.moveToNext()) {
            val day =
                scheduleCursor.getString(scheduleCursor.getColumnIndexOrThrow(ScheduleContract.ScheduleEntry.COLUMN_NAME_DAY))

            if (day != null) schedule.add(day)
        }

        scheduleCursor.close()

        return schedule
    }

    fun getItem(itemLabel: String, id: Int?): AlarmItemModel {

        lateinit var item: AlarmItemModel

        val cursor = readAppDb.query(
            ALARM_TABLE_NAME,
            null,
            if (id == null ) "${AlarmContract.AlarmEntry.COLUMN_NAME_LABEL} = ?" else "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} = ?",
            if (id == null) arrayOf(itemLabel) else arrayOf(id.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_ID))
                val hour = getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_HOUR))
                val minute =
                    getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_MINUTE))
                val label =
                    getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_LABEL))
                val ringtoneTitle =
                    getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_TITLE))
                val ringtoneUri =
                    Uri.parse(getString(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_RINGTONE_URI)))
                val isScheduled =
                    getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_IS_SCHEDULED)) != 0
                val reminder =
                    if (getInt(getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER)) != 60) getInt(
                        getColumnIndexOrThrow(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER)
                    ) else 1

                val schedule = getSchedule(itemId)

                item = AlarmItemModel(
                    itemId,
                    label,
                    hour,
                    minute,
                    schedule,
                    ringtoneTitle,
                    ringtoneUri,
                    isScheduled,
                    reminder
                )
            }
        }

        cursor.close()

        return item
    }

    fun updateReminder(id: Int, time: Int) {
        val whereClause = "${AlarmContract.AlarmEntry.COLUMN_NAME_ID} = ?"
        val whereArgs = arrayOf(id.toString())

        val values = contentValues.apply { this.put(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER, time) }

        appDB.update(ALARM_TABLE_NAME, values, whereClause, whereArgs)
    }

    fun switchAlarmOnOff(id: Int, bit: Int) {
        val whereClause = "${AlarmContract.AlarmEntry.COLUMN_NAME_IS_SCHEDULED} = ?"
        val whereArgs = arrayOf(id.toString())

        val values = contentValues.apply { this.put(AlarmContract.AlarmEntry.COLUMN_NAME_REMINDER, bit) }

        appDB.update(ALARM_TABLE_NAME, values, whereClause, whereArgs)
    }

}