package com.mejorappt.equipoa.db

import android.content.ContentValues
import android.content.Context
import com.mejorappt.equipoa.model.Result
import org.intellij.lang.annotations.Language
import java.util.Date

class ResultDAO(context: Context): DAO<Int, Result> {
    private val bh = DB(context)
    private val userDAO = UserDAO(context)

    override fun get(id: Int): Result? {
        var result: Result?  = null
        val db = bh.readableDatabase
        val c = db.rawQuery("SELECT id, userName, date, factor1, factor2, factor3, uploaded FROM result WHERE id = ?", arrayOf(id.toString()))

        if (c.moveToFirst()) {
            do {
                result = Result(c.getInt(0), userDAO.get(c.getInt(1)), Result.parseDate(c.getString(2))?:Date(), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6) == 1)
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return result
    }

    override fun getAll(): List<Result> {
        val results = ArrayList<Result>()
        val db = bh.readableDatabase
        val c = db.rawQuery("SELECT id, userName, date, factor1, factor2, factor3, uploaded FROM result", null)

        if (c.moveToFirst()) {
            do {
                results.add(Result(c.getInt(0), userDAO.get(c.getInt(1)), Result.parseDate(c.getString(2))?:Date(), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6) == 1))
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return results
    }

    fun getAll(userId: Int): List<Result> {
        val results = ArrayList<Result>()
        val db = bh.readableDatabase
        val c = db.rawQuery("SELECT id, userName, date, factor1, factor2, factor3, uploaded FROM result WHERE userName =?", arrayOf(userId.toString()))

        if (c.moveToFirst()) {
            do {
                results.add(Result(c.getInt(0), userDAO.get(c.getInt(1)), Result.parseDate(c.getString(2))?:Date(), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6) == 1))
            } while (c.moveToNext())
        }

        c.close()
        db.close()

        return results
    }

    override fun insert(values: List<Result>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("userName", it.userName?.id?: 0)
                put("date", it.formatDate())
                put("factor1", it.factor1)
                put("factor2", it.factor2)
                put("factor3", it.factor3)
                put("uploaded", it.getUploaded())
            }

            db.insert(RESULT_TABLE_NAME, null, contentValues)
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    override fun update(values: List<Result>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("userName", it.userName?.id?: 0)
                put("date", it.formatDate())
                put("factor1", it.factor1)
                put("factor2", it.factor2)
                put("factor3", it.factor3)
                put("uploaded", it.getUploaded())
            }

            val whereClause = "id = ?"

            db.update(RESULT_TABLE_NAME, contentValues, whereClause, arrayOf(it.id.toString()))
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    fun getMaxId(): Int {
        val db = bh.readableDatabase
        var id = 0
        @Language("SQL")
        val c = db.rawQuery("SELECT max(id) FROM result", null)

        if (c.moveToFirst()) {
            do {
                id = c.getInt(0)
            } while (c.moveToNext())
        }

        c.close()
        db.close()

        return ++id
    }
}