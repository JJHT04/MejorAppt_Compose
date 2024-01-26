package com.mejorappt.equipoa.db

import android.content.ContentValues
import android.content.Context
import androidx.annotation.DrawableRes
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.enums.Gender
import com.mejorappt.equipoa.model.UserProfile
import org.intellij.lang.annotations.Language
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserDAO(context: Context): DAO<Int, UserProfile>{

    private val bh = DB(context)
    private val genderMap = Gender.getDefaultGenderMap()

    override fun get(id: Int): UserProfile? {
        var user: UserProfile? = null
        val db = bh.readableDatabase
        @Language("SQL")
        val c = db.rawQuery("SELECT id, name, age, gender, icon FROM user WHERE id = ?", arrayOf(id.toString()))

        if (c.moveToFirst()) {
            do {
                val icons = getDrawableRes(c.getString(4))
                user = UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, icons.first, icons.second)
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
        val c = db.rawQuery("SELECT id, name, age, gender, icon FROM user WHERE name = ?", arrayOf(id))

        if (c.moveToFirst()) {
            do {
                val icons = getDrawableRes(c.getString(4))
                user = UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, icons.first, icons.second)
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
        val c = db.rawQuery("SELECT id, name, age, gender, icon, date, uploaded FROM user", null)

        if (c.moveToFirst()) {
            do {
                val icons = getDrawableRes(c.getString(4))
                users.add(UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, icons.first, icons.second, SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH).parse(c.getString(5))?: Date(), c.getInt(6)))
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
                put("icon", getDrawableName(it.happyIcon))
                put("date", SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH).format(
                    it.date
                ))
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
                put("icon", getDrawableName(it.happyIcon))
                put("uploaded", it.uploaded)
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

    fun getAsync(): List<UserProfile> {
        val users = ArrayList<UserProfile>()
        val db = bh.readableDatabase
        @Language("SQL")
        val c = db.rawQuery("SELECT id, name, age, gender, icon, date, uploaded FROM user WHERE uploaded = 0 or uploaded = 3", null)

        if (c.moveToFirst()) {
            do {
                val icons = getDrawableRes(c.getString(4))
                users.add(UserProfile(c.getInt(0), c.getString(1), c.getInt(2), genderMap[c.getString(3)]?:Gender.NON_BINARY, icons.first, icons.second, SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH).parse(c.getString(5))?: Date(), c.getInt(6)))
            } while (c.moveToNext())
        }

        c.close()
        db.close()
        return users
    }

    companion object {
        private const val FEMALE_ICON_NAME = "female1"
        private const val FEMALE2_ICON_NAME = "female2"
        private const val MALE_ICON_NAME = "male1"
        private const val POU_ICON_NAME = "pou1"
        private const val NON_BINARY_ICON_NAME = "non_binary1"

        fun getDrawableRes(name: String): Pair<Int, Int> {
            return when (name) {
                FEMALE_ICON_NAME -> Pair(R.drawable.female_icon_happy, R.drawable.female_icon_annoyed)
                MALE_ICON_NAME -> Pair(R.drawable.male_icon_happy, R.drawable.male_icon_annoyed)
                FEMALE2_ICON_NAME -> Pair(R.drawable.female2_icon_happy, R.drawable.female2_icon_annoyed)
                POU_ICON_NAME -> Pair(R.drawable.pou_icon_happy, R.drawable.pou_icon_annoyed)
                NON_BINARY_ICON_NAME -> Pair(R.drawable.non_binary_icon_happy, R.drawable.non_binary_icon_annoyed)
                else -> Pair(-1,-1)
            }
        }

        fun getDrawableName(@DrawableRes id: Int): String {
            return when (id) {
                R.drawable.female_icon_happy -> FEMALE_ICON_NAME
                R.drawable.male_icon_happy -> MALE_ICON_NAME
                R.drawable.female2_icon_happy -> FEMALE2_ICON_NAME
                R.drawable.pou_icon_happy -> POU_ICON_NAME
                R.drawable.non_binary_icon_happy -> NON_BINARY_ICON_NAME
                else -> "Undefined"
            }
        }
    }
}