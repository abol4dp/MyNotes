package com.example.mynotes.mynotes.mynotes.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "note_db"
        private const val DB_VERSION = 1

        const val NOTES_TABLE = "Notes"
        const val NOTES_ID = "id"
        const val NOTES_DETAIL = "detail"
        const val NOTES_DELETE_STATE = "deleted" // تغییر نام ستون به is_deleted
        const val NOTES_TITLE = "title"

        const val TRUE_STATE = "1"
        const val FALSE_STATE = "0"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $NOTES_TABLE(" +
                    "$NOTES_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOTES_TITLE VARCHAR(255)," +
                    "$NOTES_DETAIL TEXT," +
                    "$NOTES_DELETE_STATE VARCHAR(1))" // تغییر نام ستون در دستور SQL
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}
