package com.github.adamr22.data.schemas

object ScheduleContract {
    object ScheduleEntry {
        const val TABLE_NAME = "schedule"
        const val COLUMN_NAME_ID = "schedule_id"
        const val COLUMN_NAME_DAY = "day"
        const val COLUMN_NAME_ALARM_ID = "alarm_id"
    }

    const val SQL_CREATE_SCHEDULE_ENTRIES = """
        CREATE TABLE IF NOT EXISTS ${ScheduleEntry.TABLE_NAME} (
            ${ScheduleEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${ScheduleEntry.COLUMN_NAME_DAY} TEXT,
            ${ScheduleEntry.COLUMN_NAME_ALARM_ID} INTEGER FOREIGN KEY REFERENCES ${AlarmContract.AlarmEntry.TABLE_NAME}${(ScheduleEntry.COLUMN_NAME_ALARM_ID)} ON DELETE CASCADE
        )
    """

    const val SQL_DELETE_SCHEDULE_ENTRIES = """
        DROP TABLE IF NOT EXISTS ${ScheduleEntry.TABLE_NAME}
    """
}