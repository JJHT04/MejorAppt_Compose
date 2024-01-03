package com.mejorappt.equipoa.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mejorappt.equipoa.util.CSV


const val QUESTION_TABLE_NAME = "question"
const val RESULT_TABLE_NAME = "result"
const val USER_TABLE_NAME = "user"

private const val SQL_CREATE_ENTRIES1 = "CREATE TABLE IF NOT EXISTS $QUESTION_TABLE_NAME (id integer PRIMARY KEY, question text, factor integer, value integer)"
private const val SQL_CREATE_ENTRIES2 = "CREATE TABLE IF NOT EXISTS $RESULT_TABLE_NAME (id integer PRIMARY KEY AUTOINCREMENT,username INT, date TEXT, factor1 integer, factor2 integer,factor3 integer, uploaded integer)"
private const val SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INT, gender TEXT, icon TEXT, date TEXT, uploaded INTEGER)"
private const val SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS $QUESTION_TABLE_NAME"
private const val SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS $RESULT_TABLE_NAME"
private const val SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS $USER_TABLE_NAME"

class DB (private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES1)
            db.execSQL(SQL_CREATE_ENTRIES2)
            db.execSQL(SQL_CREATE_USER_TABLE)

            CSV.getQuestions(context = context).forEach {
                val contentValues = ContentValues()
                contentValues.put("id", it.id)
                contentValues.put("question", it.question)
                contentValues.put("factor", it.factor)
                contentValues.put("value", it.answer)
                db.insert(QUESTION_TABLE_NAME, null, contentValues)
            }
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES1)
            db.execSQL(SQL_DELETE_ENTRIES2)
            db.execSQL(SQL_DELETE_ENTRIES3)
            onCreate(db)
        }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "p1_questions.db"
    }
}