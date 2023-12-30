package com.mejorappt.equipoa.db

import android.content.ContentValues
import android.content.Context
import com.mejorappt.equipoa.enums.Gender
import com.mejorappt.equipoa.model.UserProfile
import org.intellij.lang.annotations.Language

class UserDAO(context: Context): DAO<Int, UserProfile>{

    private val bh = DB(context)
    private val genderMap = Gender.getDefaultGenderMap()

    override fun get(id: Int): UserProfile? {
        var user: UserProfile? = null
        val db = bh.readableDatabase
        @Language("SQL")
        val c = db.rawQuery("SELECT id, name, age, gender, iconHappy, iconAnnoyed FROM user WHERE id = ?", arrayOf(id.toString()))

        if (c.moveToFirst()) {
            do {
                user = UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, c.getInt(4), c.getInt(5))
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return user
    }

    fun get(id: String): UserProfile? {
        var user: UserProfile? = null
        val db = bh.readableDatabase
        @Language("SQL")
        val c = db.rawQuery("SELECT id, name, age, gender, iconHappy, iconAnnoyed FROM user WHERE name = ?", arrayOf(id))

        if (c.moveToFirst()) {
            do {
                user = UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, c.getInt(4), c.getInt(5))
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return user
    }

    override fun getAll(): List<UserProfile> {
        val users = ArrayList<UserProfile>()
        val db = bh.readableDatabase
        @Language("SQL")
        val c = db.rawQuery("SELECT id, name, age, gender, iconHappy, iconAnnoyed FROM user", null)

        if (c.moveToFirst()) {
            do {
                users.add(UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, c.getInt(4), c.getInt(5)))
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return users
    }

    override fun insert(values: List<UserProfile>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("name", it.userName)
                put("age", it.age)
                put("gender", it.gender.toString())
                put("iconHappy", it.happyIcon)
                put("iconAnnoyed", it.annoyedIcon)
            }

            db.insert(USER_TABLE_NAME, null, contentValues)
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    override fun update(values: List<UserProfile>) {
        val db = bh.writableDatabase

        db.beginTransaction()

        values.forEach {
            val contentValues = ContentValues().apply {
                put("name", it.userName)
                put("age", it.age)
                put("gender", it.gender.toString())
                put("iconHappy", it.happyIcon)
                put("iconAnnoyed", it.annoyedIcon)
            }

            val whereClause = "id = ${it.id}"

            db.update(USER_TABLE_NAME, contentValues, whereClause, null)
        }

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    fun getMaxId(): Int {
        val db = bh.readableDatabase
        var id = 0
        @Language("SQL")
        val c = db.rawQuery("SELECT max(id) FROM user", null)

        if (c.moveToFirst()) {
            do {
                id = c.getInt(0)
            } while (c.moveToNext())
        }

        c.close()
        db.close()

        return ++id
    }

    fun userAlreadyExists(userName: String): Boolean = get(userName) != null
}