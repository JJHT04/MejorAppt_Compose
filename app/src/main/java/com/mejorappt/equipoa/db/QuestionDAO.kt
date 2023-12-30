package com.mejorappt.equipoa.db

import android.content.ContentValues
import android.content.Context
import com.mejorappt.equipoa.model.Question

class QuestionDAO(context: Context): DAO<Int, Question> {

    private val bh = DB(context)

    override fun get(id: Int): Question? {
        var question: Question? = null
        val db = bh.readableDatabase
        val cursor = db.rawQuery("SELECT id, question, factor, value FROM question WHERE id = $id", null)

        if (cursor.moveToFirst()) {
            do {
                question = Question(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return question
    }

    override fun getAll(): List<Question> {
        val questions = ArrayList<Question>()
        val db = bh.readableDatabase
        val cursor = db.rawQuery("SELECT id, question, factor, value FROM question", null)

        if (cursor.moveToFirst()) {
            do {
                questions.add(Question(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return questions
    }

    override fun insert(values: List<Question>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("id", it.id)
                put("question", it.question)
                put("factor", it.factor)
                put("value", it.answer)
            }

            db.insert(QUESTION_TABLE_NAME, null, contentValues)
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    override fun update(values: List<Question>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("question", it.question)
                put("factor", it.factor)
                put("value", it.answer)
            }

            val whereClause = "id = ?"

            db.update(QUESTION_TABLE_NAME, contentValues, whereClause, arrayOf(it.id.toString()))
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    fun isAnyQuestionAnswered(): Boolean {
        val db = bh.readableDatabase
        val cursor = db.rawQuery("SELECT value FROM question", null)

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) != -1) {
                    cursor.close()
                    db.close()
                    return true
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return false
    }

    fun isAnyQuestionNotAnswered(): Boolean {
        val db = bh.readableDatabase
        val cursor = db.rawQuery("SELECT value FROM question", null)

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == -1) {
                    cursor.close()
                    db.close()
                    return true
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return false
    }

    fun clearAll() {
        val questions = getAll()

        questions.forEach {
            it.answer = -1
        }

        update(questions)
    }

    fun getNumberOfAnsweredQuestions(): Int {
        val db = bh.readableDatabase
        val cursor = db.rawQuery("SELECT value FROM question", null)
        var answered = 0

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) != -1) {
                    answered++
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return answered
    }
}