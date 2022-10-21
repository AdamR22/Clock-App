package com.github.adamr22.data.schemas

object AlarmContract {
    object AlarmEntry {
        const val TABLE_NAME = "alarm"
        const val COLUMN_NAME_ID = "alarm_id"
        const val COLUMN_NAME_LABEL = "label"
        const val COLUMN_NAME_RINGTONE_TITLE = "ringtone_title"
        const val COLUMN_NAME_RINGTONE_URI = "ringtone_uri"
        const val COLUMN_NAME_HOUR = "hour"
        const val COLUMN_NAME_MINUTE = "minute"
        const val COLUMN_NAME_IS_SCHEDULED = "is_scheduled"
        const val COLUMN_NAME_REMINDER = "alarm_reminder"
    }

    const val SQL_CREATE_ALARM_ENTRIES = """
        CREATE TABLE IF NOT EXISTS ${AlarmEntry.TABLE_NAME} (
            ${AlarmEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${AlarmEntry.COLUMN_NAME_LABEL} TEXT,
            ${AlarmEntry.COLUMN_NAME_RINGTONE_TITLE} TEXT,
            ${AlarmEntry.COLUMN_NAME_RINGTONE_URI} TEXT,
            ${AlarmEntry.COLUMN_NAME_HOUR} INTEGER,
            ${AlarmEntry.COLUMN_NAME_MINUTE} INTEGER,
            ${AlarmEntry.COLUMN_NAME_IS_SCHEDULED} BIT,
            ${AlarmEntry.COLUMN_NAME_REMINDER} INTEGER DEFAULT 15,
        )
    """

    const val SQL_DELETE_ALARM_ENTRIES = """
        DROP TABLE IF EXISTS ${AlarmEntry.TABLE_NAME}
    """
}